package com.metis.bubble.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.metis.bubble.main.AuthInterceptor;
import com.metis.bubble.main.LoginInterceptor;
import com.metis.bubble.model._MappingKit;
import com.metis.bubble.main.MainRoutes;
import com.metis.bubble.main.PermissionDirective;

import com.jfinal.config.*;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

import java.sql.Connection;

public class BaseConfig extends JFinalConfig {
    private static Prop defaultConfig;
    private WallFilter wallFilter;
    private static Log log = Log.getLog(BaseConfig.class);

    /**
     * 启动入口，运行此 main 方法可以启动项目，此 main 方法可以放置在任意的 Class 类定义中，不一定要放于此
     */
    public static void main(String[] args) {
        UndertowServer.start(BaseConfig.class);
    }


    public static void setupEnv() {
        // 在不启动 web 服务下，使用 AR 建立与数据库的连接；
        loadConfig();

        DruidPlugin druidPlugin = getDruidPlugin();

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
        _MappingKit.mapping(arp);

        arp.setShowSql(false);

        arp.getEngine().setToClassPathSourceFactory();
        arp.addSqlTemplate("/sql/_all.sql");

        // 与 jfinal web 环境唯一的不同是要手动调用一次相关插件的start()方法
        druidPlugin.start();
        arp.start();
    }

    private static void loadConfig() {
        if (defaultConfig == null) {
            defaultConfig = PropKit.useFirstFound("production.properties", "default.properties");
        }
    }

    /**
     * 配置JFinal常量值
     * @param me
     */
    @Override
    public void configConstant(Constants me) {
        /// 设置 devMode 之下的 action report 是否在 invocation 之后，默认值为 true
        me.setReportAfterInvocation(false);
        defaultConfig = PropKit.use("default.properties");

        me.setDevMode(defaultConfig.getBoolean("devMode", false));

        // 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
        me.setInjectDependency(true);

        // 是否对超类中的属性进行注入
        me.setInjectSuperClass(true);

        me.setJsonFactory(new MixedJsonFactory());
        me.setRenderFactory(new ExceptionRenderFactory());
    }

    /**
     * 配置访问路由
     * @param me
     */
    @Override
    public void configRoute(Routes me) {
        me.setBaseViewPath("/view");

        me.add(new MainRoutes());
    }

    /**
     * 配置引擎
     * @param me
     */
    @Override
    public void configEngine(Engine me) {
        me.setDevMode(defaultConfig.getBoolean("devMode", false));

        // 添加角色、权限指令
        me.addDirective("permission", PermissionDirective.class);

        //设置共享页面
        me.addSharedFunction("/view/common/_main.html");
    }

    public static DruidPlugin getDruidPlugin() {
        loadConfig();
        //配置数据库连接池
        DruidPlugin dp = new DruidPlugin(defaultConfig.get("jdbcUrl"), defaultConfig.get("user"), defaultConfig.get("password"), "com.mysql.cj.jdbc.Driver");
        return dp;
    }


    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin druidPlugin = getDruidPlugin();

        if (defaultConfig.getBoolean("devMode", false)) {
            //打印完整sql语句，核心语句就这一句
            druidPlugin.addFilter(new MyDruidFilter());
        }

        // 加强数据库安全
        wallFilter = new WallFilter();
        wallFilter.setDbType("mysql");
        druidPlugin.addFilter(wallFilter);
        // 添加 StatFilter 才会有统计数据
        druidPlugin.addFilter(new StatFilter());
        me.add(druidPlugin);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        /// arp.setDevMode(defaultConfig.getBoolean("devMode", false));
        arp.setDevMode(true);

        arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
        _MappingKit.mapping(arp);

        me.add(arp);
        arp.setShowSql(defaultConfig.getBoolean("devMode", false));

        arp.getEngine().setToClassPathSourceFactory();
        arp.addSqlTemplate("/sql/_all.sql");

        if (ZCacheKit.useCache()) {
            System.out.println("use cache...");
            me.add(new EhCachePlugin());
        }
    }

    @Override
    public void configInterceptor(Interceptors me) {
        // 全局拦截器
        me.add(new LoginInterceptor());
        me.add(new AuthInterceptor());
    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}

