/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

package com.metis.bubble.model;

import com.metis.bubble.config.BaseConfig;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * Model、BaseModel、_MappingKit 生成器
 */
public class _Generator {

	/**
	 * 部分功能使用 Db + Record 模式实现，无需生成 model 的 table 在此配置
	 */
	private static String[] excludedTable = {
		"bom_info", "bom_mat",
		"plant_info",
		"po_mat_stats", "po_vendor_mat", "po_vendor_mat_true", "po_vendor_plant_client",
		"po_vendor_project", "po_vendor_stats",
		"vendor_info",
		"z_resources"
	};

	/**
	 * 重用 JFinalClubConfig 中的数据源配置，避免冗余配置
	 */
	public static DataSource getDataSource() {
		DruidPlugin druidPlugin = BaseConfig.getDruidPlugin();
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "com.demo.model.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath()
				+ "/src/main/java/com/demo/model/base";

		System.out.println("输出路径："+ baseModelOutputDir);

		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.demo.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";

		// 创建生成器
		Generator gen = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);

		// 这里可以指定生成某些张表，而不是全部生成
		// gen.setMetaBuilder(new _MetaBuilder(getDataSource()));

		// 设置数据库方言
		gen.setDialect(new MysqlDialect());
		
		// 设置是否生成字段备注
		gen.setGenerateRemarks(true);
		
		// 添加不需要生成的表名
		for (String table : excludedTable) {
			gen.addExcludedTable(table.trim());
		}
		
		// 设置是否在 Model 中生成 dao 对象
		gen.setGenerateDaoInModel(false);
		
		// 设置是否生成字典文件
		gen.setGenerateDataDictionary(false);
		
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		// gernerator.setRemovedTableNamePrefixes("t_");
		
		// 生成
		gen.generate();
	}
}
