package com.pvn;

import com.vaadin.annotations.Theme;
import com.vaadin.client.metadata.*;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.data.*;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by strelkon on 2/9/2017.
 */
@Theme("mytheme")
public class game extends UI {
    private final ConcurrentHashMap <Integer, Player> players = new ConcurrentHashMap <Integer, Player>();
    private final ConcurrentHashMap <Integer, Tree> trees = new ConcurrentHashMap <Integer, Tree>();
    private final ConcurrentHashMap <Integer, Label> labels = new ConcurrentHashMap <Integer, Label>();
    private int decisionsMade;
    private final String[][] strategies = new String[][]{new String[]{"A","A1","A2"},new String[]{"B","B1","B2"}};
    @Override
    public void init(VaadinRequest request) {
        for (int i=0; i<4; i++){
            Player player = new Player(Integer.toString(i));
            players.put(i, player);
        }
        initStrategies();

        VerticalLayout root = new VerticalLayout();
        //setContent(layout);
        setContent(root);
        //
        for (Player player: players.values()) {
            //layout.addComponent(new TextArea("Log"));
            HorizontalLayout layout = new HorizontalLayout();
            Table table = new Table("Player "+player.id);

            // Define two columns for the built-in container
            table.addContainerProperty("Priority", String.class, null);
            table.addContainerProperty("Decrease", Button.class, null);
            table.addContainerProperty("Value", Label.class, null);
            table.addContainerProperty("Increase", Button.class, null);

            // Add a row the hard way
            /*Object newItemId = table.addItem();
            Item row1 = table.getItem(newItemId);
            row1.getItemProperty("Name").setValue("Sirius");
            row1.getItemProperty("Mag").setValue(-1.46f);
            */

            // Add a few other rows using shorthand addItem()
            for (int j=0; j<player.getPriorities().length; j++) {
                Button b1 = new Button("-");
                Button b2 = new Button("+");
                final int k = j;
                Property property = new Property() {
                    @Override
                    public Object getValue() {
                        return player.getPriority(k);
                    }

                    @Override
                    public void setValue(Object o) throws ReadOnlyException {

                    }

                    @Override
                    public Class getType() {
                        return Integer.class;
                    }

                    @Override
                    public boolean isReadOnly() {
                        return false;
                    }

                    @Override
                    public void setReadOnly(boolean b) {

                    }
                };
                Label label = new Label(property);

                b1.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        player.decreasePriority(k);
                        Property prop = new Property() {
                            @Override
                            public Object getValue() {
                                return player.getPriority(k);
                            }

                            @Override
                            public void setValue(Object o) throws ReadOnlyException {

                            }

                            @Override
                            public Class getType() {
                                return Integer.class;
                            }

                            @Override
                            public boolean isReadOnly() {
                                return false;
                            }

                            @Override
                            public void setReadOnly(boolean b) {

                            }
                        };
                        label.setPropertyDataSource(prop);
                        if ((int) prop.getValue() < 1){
                            b1.setEnabled(false);
                        }
                        if ((int) prop.getValue() < 3){
                            b2.setEnabled(true);
                        }
                    }
                });
                b2.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        player.increasePriority(k);
                        Property prop = new Property() {
                            @Override
                            public Object getValue() {
                                return player.getPriority(k);
                            }

                            @Override
                            public void setValue(Object o) throws ReadOnlyException {

                            }

                            @Override
                            public Class getType() {
                                return Integer.class;
                            }

                            @Override
                            public boolean isReadOnly() {
                                return false;
                            }

                            @Override
                            public void setReadOnly(boolean b) {

                            }
                        };
                        label.setPropertyDataSource(prop);
                        if ((int) prop.getValue() > 2){
                            b2.setEnabled(false);
                        }
                        if ((int) prop.getValue() > 0){
                            b1.setEnabled(true);
                        }
                    }
                });

                table.addItem(new Object[]{"Stickniness of clients", b1, label, b2}, j + 2);
            }

            // Show exactly the currently contained rows (items)
            table.setPageLength(table.size());
            setContent(layout);
            layout.addComponent(table);

            //Strategy Tree
            Tree tree = new Tree("Strategies");
            for (int i=0; i<strategies.length; i++) {
                String strategy = (String) (strategies[i][0]);
                tree.addItem(strategy);
                if (strategies[i].length == 1) {
                    tree.setChildrenAllowed(strategy, false);
                }
                else {
                    // Add children.
                    for (int j=1; j<strategies[i].length; j++) {
                        String subStrategy = (String) strategies[i][j];
                        // Add the item as a regular item.
                        tree.addItem(subStrategy);
                        // Set it to be a child.
                        tree.setParent(subStrategy, strategy);
                        tree.setChildrenAllowed(subStrategy, false);
                    }
                }
                // Expand the subtree.
                //tree.expandItemsRecursively(strategy);
            }
            trees.put(player.hashCode(),tree);
            tree.setId(player.id);
            tree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
                @Override
                public void itemClick(ItemClickEvent itemClickEvent) {
                    labels.get(player.hashCode()).setValue("A1");
                }
            });
            tree.setImmediate(true);

            //
            layout.setSpacing(true);
            layout.addComponent(tree);
            tree.setVisible(false);

            //
            Label label = new Label("labe");
            labels.put(player.hashCode(),label);
            label.setId(player.id);
            layout.addComponent(label);

            Button b3 = new Button("Make decision");
            b3.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    decisionsMade++;
                    b3.setEnabled(false);
                    System.out.println("Players have moved="+decisionsMade);
                    if (decisionsMade == players.size()){
                        for (Tree tr:trees.values()){
                            tr.setVisible(true);
                        }
                    }
                }
            });
            setContent(root);
            root.addComponent(layout);
            root.addComponent(b3);
            root.setSpacing(true);
        }
    }

    private void initStrategies (){
        Strategy s1 = new Strategy("A");
        Strategy s2 = new Strategy("A1");
        Strategy s3 = new Strategy("A1");
        Strategy s4 = new Strategy("B");
        Strategy s5 = new Strategy("B1");
        Strategy s6 = new Strategy("B2");
        s1.setShortDescription("");
    }

    private void showStrategies(Tree tree){
        tree.setVisible(true);
    }

}
