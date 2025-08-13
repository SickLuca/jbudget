package it.unicam.cs.mpgc.jbudget119250;

import it.unicam.cs.mpgc.jbudget119250.Controller.Controller;
import it.unicam.cs.mpgc.jbudget119250.Controller.DefaultJpaController;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractCategory;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultCategory;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultMovement;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultTag;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Scenes/DefaultScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        //addTagsAndCategories();
        //addProfiles();
        //removeNullMovements();
        //removeDuplicateProfiles();
        //removeDuplicateTags();
        //printAllTags();
        //removeDuplicateCategories();
        //printAllCategories();
        //removeTags();
        //removeAllMovements();
        //removeTag();
        //removeCategory();
        launch();

    }

    private static void removeTag() {
        Controller<DefaultTag> tagController = new DefaultJpaController<>(DefaultTag.class);
        tagController.getAll().forEach(tag -> {if (tag.getName().equals("CASA > CASA")) {
            tagController.delete(tag.getId());
        }});
    }

    private static void removeCategory() {
        Controller<DefaultCategory> categoryController = new DefaultJpaController<>(DefaultCategory.class);
        categoryController.delete(939L);
    }

    private static void removeAllMovements() {
        Controller<AbstractMovement> movementController = new DefaultJpaController<>(AbstractMovement.class);
        for (AbstractMovement movement : movementController.getAll() ) {
            movementController.delete(movement.getId());
        }
//        Controller<DefaultMovement> movementController = new DefaultJpaController<>(DefaultMovement.class);
//        List<DefaultMovement> movements = movementController.getAll();
//        for (DefaultMovement movement : movements) {
//            System.out.println(movement.getId());
//        }
    }

    private static void printAllCategories() {
        Controller<DefaultCategory> categoryController = new DefaultJpaController<>(DefaultCategory.class);
        List<DefaultCategory> categories = categoryController.getAll();
        for (DefaultCategory category : categories) {
            System.out.println(category.getId() + " " + category.getName());
        }
    }

    private static void removeTags() {
        Controller<DefaultTag> tagController = new DefaultJpaController<>(DefaultTag.class);
        for (DefaultTag tag : tagController.getAll() ) {
                tagController.delete(tag.getId());
            }
    }

    private static void printAllTags() {
        Controller<DefaultTag> tagController = new DefaultJpaController<>(DefaultTag.class);
        List<DefaultTag> tags = tagController.getAll();
        for (DefaultTag tag : tags) {
            System.out.println(tag.getId() + " " + tag.getName());
        }
    }

    private static void addTagsAndCategories(){
        DefaultCategory category1 = new DefaultCategory();
        category1.setName("CASA");

        DefaultCategory category2 = new DefaultCategory();
        DefaultCategory category3 = new DefaultCategory();
        category2.setName("SPESA");
        category3.setName("BOLLETTE");

        Controller<DefaultCategory> categoryController = new DefaultJpaController<>(DefaultCategory.class);
        categoryController.save(category1);
        categoryController.save(category2);
        categoryController.save(category3);

        category1.addChild(category2);
        category1.addChild(category3);

        categoryController.update(category1);


        DefaultTag tag1 = new DefaultTag();
        DefaultTag  tag2 = new DefaultTag();
        DefaultTag  tag3 = new DefaultTag();

        tag1.setCategory(category1);
        tag1.setName(category1.getFullPath());

        tag2.setCategory(category2);
        tag2.setName(category2.getFullPath());

        tag3.setCategory(category3);
        tag3.setName(category3.getFullPath());

        Controller<DefaultTag> tagController = new DefaultJpaController<>(DefaultTag.class);
        tagController.save(tag1);
        tagController.save(tag2);
        tagController.save(tag3);
    }

    private static void addProfiles(){
        DefaultUser user1 = new DefaultUser();
        user1.setName("Luca");
        user1.setSurname("Repupilli");

        DefaultUser user2 = new DefaultUser();
        user2.setName("Michele");
        user2.setSurname("Loreti");

        DefaultUser user3 = new DefaultUser();
        user3.setName("Lorenzo");
        user3.setSurname("Rossi");

        Controller<DefaultUser> userController = new DefaultJpaController<>(DefaultUser.class);
        userController.save(user1);
        userController.save(user2);
        userController.save(user3);

    }

    private static void removeNullMovements() {
        Controller<AbstractMovement> movementController = new DefaultJpaController<>(AbstractMovement.class);
        for (AbstractMovement movement : movementController.getAll() ) {
            if (movement.getTag().isEmpty()) {
                movementController.delete(movement.getId());
            }
        }
    }

    private static void removeDuplicateTags() {
        Controller<DefaultTag> tagController = new DefaultJpaController<>(DefaultTag.class);
        for (DefaultTag tag : tagController.getAll() ) {
            if (tag.getId() > 3) {
                tagController.delete(tag.getId());
            }
        }
    }

    private static void removeDuplicateCategories() {
        Controller<DefaultCategory> categoryController = new DefaultJpaController<>(DefaultCategory.class);
        for (DefaultCategory category : categoryController.getAll() ) {
            if (category.getId()>0) {
                categoryController.delete(category.getId());
            }
        }
    }

    private static void removeDuplicateProfiles() {
        Controller<DefaultUser> userController = new DefaultJpaController<>(DefaultUser.class);
        for (DefaultUser user : userController.getAll() ) {
            if (user.getId() > 3) {
                userController.delete(user.getId());
            }
        }
    }


}

