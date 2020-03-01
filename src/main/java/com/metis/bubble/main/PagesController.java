package com.metis.bubble.main;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.metis.bubble.wordcloud.CloudService;
import com.metis.bubble.wordcloud.CloudWord;

import java.util.List;

/**
 * @author ygzheng
 */
public class PagesController extends Controller {
    @Inject
    private
    CloudService cloudSvc;

    public void wordCloud() {
        render("word_cloud.html");
    }

    public void wordCloudData() {
        String force = get("force");

        boolean isForce = false;
        if (force != null && force.compareTo("true") == 0) {
            isForce = true;
        }

        List<CloudWord> words = cloudSvc.getCloud(isForce);

        renderJson(words);
    }

}
