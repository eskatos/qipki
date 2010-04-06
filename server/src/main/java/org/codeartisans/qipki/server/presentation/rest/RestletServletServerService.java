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
package org.codeartisans.qipki.server.presentation.rest;

import javax.servlet.Servlet;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.object.ObjectBuilder;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.service.ServiceComposite;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.ext.servlet.ServerServlet;

@Mixins( RestletServletServerService.Mixin.class )
public interface RestletServletServerService
        extends Servlet, ServiceComposite
{

    abstract class Mixin
            extends ServerServlet
            implements RestletServletServerService
    {

        @Structure
        private ObjectBuilderFactory obf;

        @Override
        protected Application createApplication( Context context )
        {
            ObjectBuilder<Application> app = obf.newObjectBuilder( Application.class );
            app.use( context.createChildContext(), getServletConfig(), getServletContext() );
            return app.newInstance();
        }

    }

}
