package org.codeartisans.qipki.ca.presentation.rest.resources.ca;

import java.util.Collections;
import org.codeartisans.qipki.ca.application.contexts.CAContext;
import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.domain.crl.CRL;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractEntityResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractResource;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CRLResource
        extends AbstractResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CRLResource.class );

    public CRLResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
        setAllowedMethods( Collections.singleton( Method.GET ) );
        setNegotiated( false );
        getVariants().add( new Variant( MediaType.TEXT_PLAIN ) );
    }

    @Override
    protected Representation get()
            throws ResourceException
    {
        try {

            // Data
            String caIdentity = ensureRequestAttribute( AbstractEntityResource.PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );

            // Context
            RootContext rootCtx = newRootContext();
            CAContext caCtx = rootCtx.caContext( caIdentity );

            // Interaction
            CRL crl = caCtx.ca().crl().get();

            // Representation
            return new StringRepresentation( crl.pem().get(), MediaType.TEXT_PLAIN );

        } catch ( NoSuchEntityException ex ) {
            LOGGER.trace( "404: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.CLIENT_ERROR_NOT_FOUND );
        }
    }

}
