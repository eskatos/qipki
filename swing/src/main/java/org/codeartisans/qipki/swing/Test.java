/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
