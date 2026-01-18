package com.todolist.pages;

import com.todolist.TodoSession;
import com.todolist.dao.TaskDAO;
import com.todolist.entities.Task;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

@AuthorizeInstantiation("SIGNED_IN")
public class AddTaskPage extends BasePage {

    private final TaskDAO taskDAO = new TaskDAO();

    public AddTaskPage() {
        // We wrap a new Task object in a CompoundPropertyModel
        Task newTask = new Task();
        Form<Task> addForm = new Form<>("addForm", new CompoundPropertyModel<>(newTask)) {
            @Override
            protected void onSubmit() {
                // Set the owner and default status
                newTask.setUserId(TodoSession.get().getUser().getId());
                newTask.setStatus("PENDING");
                
                // Save to Database
                taskDAO.save(newTask);
                
                // Return to Home
                setResponsePage(HomePage.class);
            }
        };

        addForm.add(new TextField<String>("description").setRequired(true));
        add(addForm);
    }
}