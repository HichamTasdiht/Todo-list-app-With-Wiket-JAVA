package com.todolist.pages;

import com.todolist.TodoSession;
import com.todolist.dao.TaskDAO;
import com.todolist.entities.Task;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import java.util.Arrays;
import java.util.List;

@AuthorizeInstantiation("SIGNED_IN")
public class HomePage extends BasePage {
    private String searchTerm = "";
    private String selectedStatus = "ALL";
    private final List<String> STATUS_OPTIONS = Arrays.asList("ALL", "PENDING", "DONE");

    public HomePage() {
        TaskDAO taskDAO = new TaskDAO();
        Long userId = TodoSession.get().getUser().getId();

        // 1. Statistics Labels
        add(new Label("totalTasks", new PropertyModel<>(taskDAO.countTasks(userId, "ALL"), "")));
        add(new Label("doneTasks", new PropertyModel<>(taskDAO.countTasks(userId, "DONE"), "")));

        // 2. Add Task Link
        add(new Link<Void>("addTask") {
            @Override public void onClick() { setResponsePage(AddTaskPage.class); }
        });

        // 3. Search Form
        Form<Void> searchForm = new Form<Void>("searchForm") {
            @Override protected void onSubmit() { /* Refresh automatically */ }
        };
        searchForm.add(new TextField<>("searchInput", new PropertyModel<>(this, "searchTerm")));
        searchForm.add(new DropDownChoice<>("statusFilter", new PropertyModel<>(this, "selectedStatus"), STATUS_OPTIONS));
        add(searchForm);

        // 4. Tasks Table
        ListDataProvider<Task> provider = new ListDataProvider<Task>() {
            @Override protected List<Task> getData() {
                return taskDAO.findTasks(userId, searchTerm, selectedStatus);
            }
        };

        DataView<Task> dataView = new DataView<Task>("taskGroup", provider) {
            @Override
            protected void populateItem(Item<Task> item) {
                Task task = item.getModelObject();
                item.add(new Label("desc", task.getDescription()));
                
                Label statusLabel = new Label("status", task.getStatus());
                item.add(statusLabel);

                item.add(new Link<Void>("toggle") {
                    @Override public void onClick() {
                        taskDAO.toggleStatus(task.getId(), task.getStatus());
                    }
                });

                item.add(new Link<Void>("delete") {
                    @Override public void onClick() {
                        taskDAO.delete(task.getId());
                    }
                });
            }
        };
        add(dataView);
    }
}