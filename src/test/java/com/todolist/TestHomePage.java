package com.todolist;

import com.todolist.pages.HomePage;
import com.todolist.pages.LoginPage;
import com.todolist.entities.User;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage {
    private WicketTester tester;

    @BeforeEach
    public void setUp() {
        // Initialize the tester with your WicketApplication
        tester = new WicketTester(new WicketApplication());
    }

    @Test
    public void homepageRendersSuccessfullyForLoggedInUser() {
        // 1. Simulate a logged-in user in the session
        TodoSession session = (TodoSession) tester.getSession();
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        
        // Use a custom method in your TodoSession or manually set the user
        session.setUser(mockUser); 

        // 2. Start and render the test page
        tester.startPage(HomePage.class);

        // 3. Assert that the response page is actually HomePage
        tester.assertRenderedPage(HomePage.class);
        
        // 4. Check if the "Welcome" label contains the user's name
        tester.assertContains("Test");
    }

    @Test
    public void homepageRedirectsToLoginWhenNotAuthenticated() {
        // Clear session to ensure no one is logged in
        tester.getSession().invalidate();

        // Try to access the protected HomePage
        tester.startPage(HomePage.class);

        // Assert that the user was redirected to the LoginPage
        tester.assertRenderedPage(LoginPage.class);
    }
}