package com.todolist.pages;


import com.todolist.entities.User;
import com.todolist.dao.UserDAO;

import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class RegisterPage extends BasePage {

    private final UserDAO userDAO = new UserDAO();

    public RegisterPage() {
        FeedbackPanel feedback = new FeedbackPanel("feedback") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                // Hide the component if there are no messages to show
                setVisible(anyMessage());
            }
        };
        add(feedback);

        // Create a new User object to hold the form data
        User newUser = new User();

        // The form uses a CompoundPropertyModel which maps HTML wicket:ids to User object properties
        Form<User> registerForm = new Form<User>("registerForm", new CompoundPropertyModel<>(newUser)) {
            @Override
            protected void onSubmit() {
                User userToSave = getModelObject();
                
                // Save the user to the MySQL database
                userDAO.save(userToSave);
                
                // After successful registration, send them to the Login page
                setResponsePage(LoginPage.class);
            }
        };

        // Add form components
        registerForm.add(new TextField<>("firstName").setRequired(true));
        registerForm.add(new TextField<>("lastName").setRequired(true));
        registerForm.add(new EmailTextField("email").setRequired(true));
        
        // PasswordTextField automatically hides the input and is required by default
        registerForm.add(new PasswordTextField("password"));

        add(registerForm);

        // Link to go back to Login page
        add(new BookmarkablePageLink<>("loginLink", LoginPage.class));
    }
}