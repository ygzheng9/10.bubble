package com.metis.bubble.main;

import com.jfinal.aop.Before;
import com.metis.bubble.inbound.InboundController;
import com.metis.bubble.mat.MatController;
import com.jfinal.config.Routes;
import com.metis.bubble.relation.RelationController;

/**
 * @author ygzheng
 */

public class MainRoutes extends Routes {
    @Override
    public void config() {
        setBaseViewPath("/view/main");

        add("/", MainController.class);

        add("/pages", PagesController.class, "/pages");

        // bom 共用性分析
        add("/pages/mat", MatController.class, "/pages/mat");

        // 入库分析
        add("/pages/inbound", InboundController.class, "/pages/inbound");

        // 关系图分析
        add("/pages/relation", RelationController.class, "/pages/relation");
    }
}
