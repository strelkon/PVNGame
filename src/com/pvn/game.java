package com.pvn;

import com.vaadin.client.metadata.*;
import com.vaadin.data.Property;
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
public class game extends UI {
    private final ConcurrentHashMap <Integer, Player> players = new ConcurrentHashMap <Integer, Player>();
    private int decisionsMade;
    @Override
    public void init(VaadinRequest request) {
        for (int i=0; i<4; i++){
            Player player = new Player(Integer.toString(i));
            players.put(i, player);
        }
        HorizontalLayout root = new HorizontalLayout();
        VerticalLayout layout = new VerticalLayout();
        //setContent(layout);
        setContent(root);
        layout.addComponent(new Label("Hello, world!"));
        //
        for (Player player: players.values()) {
            //layout.addComponent(new TextArea("Log"));
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
            layout.addComponent(table);

            Button b3 = new Button("Make decision");
            b3.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    decisionsMade++;
                    b3.setEnabled(false);
                    System.out.println("Players have moved="+decisionsMade);
                }
            });
            layout.addComponent(b3);
        }
        root.addComponent(layout);
        root.addComponent(new Label("labe"));
    }
}
