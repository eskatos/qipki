package org.qipki.clients.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;

import org.qipki.clients.web.shared.FieldVerifier;

public class qipkiweb
        implements EntryPoint
{

    private final Messages messages = GWT.create( Messages.class );

    public void onModuleLoad()
    {
        final Button sendButton = new Button( "Send" );
        final TextBox nameField = new TextBox();
        nameField.setText( "Enter your name" );
        final Label errorLabel = new Label();

        // We can add style names to widgets
        sendButton.addStyleName( "sendButton" );

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get( "nameFieldContainer" ).add( nameField );
        RootPanel.get( "sendButtonContainer" ).add( sendButton );
        RootPanel.get( "errorLabelContainer" ).add( errorLabel );

        // Focus the cursor on the name field when the app loads
        nameField.setFocus( true );
        nameField.selectAll();

        // Create the popup dialog box
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText( "Remote Procedure Call" );
        dialogBox.setAnimationEnabled( true );
        final Button closeButton = new Button( "Close" );
        // We can set the id of a widget by accessing its Element
        closeButton.getElement().setId( "closeButton" );
        final Label textToServerLabel = new Label();
        final HTML serverResponseLabel = new HTML();
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName( "dialogVPanel" );
        dialogVPanel.add( new HTML( "<b>Sending name to the server:</b>" ) );
        dialogVPanel.add( textToServerLabel );
        dialogVPanel.add( new HTML( "<br><b>Server replies:</b>" ) );
        dialogVPanel.add( serverResponseLabel );
        dialogVPanel.setHorizontalAlignment( VerticalPanel.ALIGN_RIGHT );
        dialogVPanel.add( closeButton );
        dialogBox.setWidget( dialogVPanel );

        // Add a handler to close the DialogBox
        closeButton.addClickHandler( new ClickHandler()
        {

            public void onClick( ClickEvent event )
            {
                dialogBox.hide();
                sendButton.setEnabled( true );
                sendButton.setFocus( true );
            }

        } );

        // Create a handler for the sendButton and nameField
        class MyHandler
                implements ClickHandler, KeyUpHandler
        {

            /**
             * Fired when the user clicks on the sendButton.
             */
            public void onClick( ClickEvent event )
            {
                sendNameToServer();
            }

            /**
             * Fired when the user types in the nameField.
             */
            public void onKeyUp( KeyUpEvent event )
            {
                if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ) {
                    sendNameToServer();
                }
            }

            /**
             * Send the name from the nameField to the server and wait for a response.
             */
            private void sendNameToServer()
            {
                // First, we validate the input.
                errorLabel.setText( "" );
                String textToServer = nameField.getText();
                if ( !FieldVerifier.isValidName( textToServer ) ) {
                    errorLabel.setText( "Please enter at least four characters" );
                    return;
                }

                // Then, we send the input to the server.
                sendButton.setEnabled( false );
                textToServerLabel.setText( textToServer );
                serverResponseLabel.setText( "" );
                dialogBox.setText( "Remote Procedure Call" );
                serverResponseLabel.removeStyleName( "serverResponseLabelError" );
                serverResponseLabel.setHTML( "Hahgahagkah deleted :)" );
                dialogBox.center();
                closeButton.setFocus( true );

                // ****************************************************************************************
                getApi();
                // ****************************************************************************************

            }

        }

        // Add a handler to send the name to the server
        MyHandler handler = new MyHandler();
        sendButton.addClickHandler( handler );
        nameField.addKeyUpHandler( handler );
    }

    private void getApi()
    {
        Resource resource = new Resource( GWT.getModuleBaseURL() + "../api" );
        resource.get().send( new JsonCallback()
        {

            public void onFailure( Method method, Throwable exception )
            {
                Window.alert( "ERROR: " + exception.getMessage() );
            }

            public void onSuccess( Method method, JSONValue response )
            {
                Window.alert( "SUCCESS: " + response.toString() );
            }

        } );
    }

}
