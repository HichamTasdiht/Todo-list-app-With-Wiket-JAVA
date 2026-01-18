package com.todolist.pages;


import com.todolist.TodoSession;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.resource.ContextRelativeResourceReference;

public class BasePage extends WebPage {

    public BasePage() {
        
        // Display the logged-in user's name in the header
        String displayUser = "Guest";
        if (TodoSession.get().isSignedIn()) {
            displayUser = TodoSession.get().getUser().getFirstName();
        }
        add(new Label("username", displayUser));

        add(new Link<Void>("homePage") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });

        // Global Logout Link
        add(new Link<Void>("logout") {
            @Override
            public void onClick() {
                TodoSession.get().invalidate(); // Clear session
                setResponsePage(LoginPage.class);
            }
            
            @Override
            protected void onConfigure() {
                super.onConfigure();
                // Only show logout link if user is actually signed in
                setVisible(TodoSession.get().isSignedIn());
            }
        });
    }
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        
        // This tells Wicket to look in the webapp/js folder
        response.render(JavaScriptHeaderItem.forReference(
            new ContextRelativeResourceReference("js/main.js", false)));
    }
}