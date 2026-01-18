package com.todolist;

import com.todolist.pages.HomePage;
import com.todolist.pages.LoginPage;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.csp.CSPDirective;
import org.apache.wicket.csp.CSPDirectiveSrcValue;
import org.apache.wicket.markup.html.WebPage;

public class WicketApplication extends AuthenticatedWebApplication {

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected void init() {
        super.init();
        
        // Required for @AuthorizeInstantiation to work
        getSecuritySettings().setAuthorizationStrategy(
            new org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy(this)
        );

        // 1. Keep CSP disabled so it doesn't block scripts
        getCspSettings().blocking().disabled();

        mountResource("/js/main.js", new org.apache.wicket.request.resource.PackageResourceReference(getClass(), "js/main.js"));
        
        getCspSettings().blocking()
            .add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF)
            .add(CSPDirective.STYLE_SRC, "https://cdn.jsdelivr.net")
            .add(CSPDirective.FONT_SRC, "https://cdn.jsdelivr.net");
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return TodoSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }
}