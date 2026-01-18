package com.todolist.pages;


import com.todolist.TodoSession;

import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class LoginPage extends BasePage {

    private String email;
    private String password;

    public LoginPage() {
        FeedbackPanel feedback = new FeedbackPanel("feedback") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                // Hide the component if there are no messages to show
                setVisible(anyMessage());
            }
        };
        add(feedback);

        // Create the Login Form
        Form<Void> loginForm = new Form<Void>("loginForm") {
            @Override
            protected void onSubmit() {
                TodoSession session = TodoSession.get();
                
                // The signIn method calls the authenticate() logic in TodoSession
                if (session.signIn(email, password)) {
                    // Redirect to the dashboard on success
                    setResponsePage(HomePage.class);
                } else {
                    // Show error message on failure
                    error("Login failed. Please check your email and password.");
                }
            }
        };

        // Bind the text fields to the class variables using PropertyModels
        loginForm.add(new EmailTextField("email", new PropertyModel<>(this, "email")).setRequired(true));
        loginForm.add(new PasswordTextField("password", new PropertyModel<>(this, "password")));

        add(loginForm);

        // Link to navigate to the Register Page
        add(new BookmarkablePageLink<>("registerLink", RegisterPage.class));
    }
}