/*
 * Copyright (c) 2010, Paul Merlin. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codeartisans.qipki.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceComposite;

@Mixins( Test.Mixin.class )
public interface Test
        extends Activatable, ServiceComposite
{

    abstract class Mixin
            implements Test
    {

        @Override
        public void activate()
                throws Exception
        {
            SwingUtilities.invokeLater( new Runnable()
            {

                @Override
                public void run()
                {
                    final JFrame frame = new JFrame();
                    JButton button = new JButton( "ClickMe" );
                    button.addActionListener( new ActionListener()
                    {

                        @Override
                        public void actionPerformed( ActionEvent e )
                        {
                            try {
                                JOptionPane.showMessageDialog( frame, enroll(), "Generated certificate", JOptionPane.INFORMATION_MESSAGE );
                            } catch ( IOException ex ) {
                                JOptionPane.showMessageDialog( frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
                            }
                        }

                    } );
                    frame.add( button );
                    frame.pack();
                    frame.setVisible( true );
                }

            } );
        }

        @Override
        public void passivate()
                throws Exception
        {
        }

        private String enroll()
                throws IOException
        {
            HttpHost host = new HttpHost( "localhost", 8443 );
            HttpGet get = new HttpGet( "/api/ra" );
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            DefaultHttpClient client = new DefaultHttpClient();
            return client.execute( host, get, responseHandler );
        }

    }

}
