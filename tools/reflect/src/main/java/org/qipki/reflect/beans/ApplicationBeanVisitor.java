/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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
package org.qipki.reflect.beans;

import org.qi4j.api.common.InvalidApplicationException;
import org.qi4j.spi.composite.AbstractCompositeDescriptor;
import org.qi4j.spi.composite.TransientDescriptor;
import org.qi4j.spi.entity.EntityDescriptor;
import org.qi4j.spi.object.ObjectDescriptor;
import org.qi4j.api.service.ImportedServiceDescriptor;
import org.qi4j.spi.service.ServiceDescriptor;
import org.qi4j.spi.structure.ApplicationDescriptor;
import org.qi4j.spi.structure.DescriptorVisitor;
import org.qi4j.spi.structure.LayerDescriptor;
import org.qi4j.spi.structure.ModuleDescriptor;
import org.qi4j.spi.value.ValueDescriptor;

public class ApplicationBeanVisitor
        extends DescriptorVisitor<InvalidApplicationException>
{

    private LayerBean currentLayerBean;
    private ModuleBean currentModuleBean;
    private ApplicationBean app;

    @Override
    public void visit( ApplicationDescriptor applicationDescriptor )
            throws InvalidApplicationException
    {
        app = new ApplicationBean();
        app.name = applicationDescriptor.name();
    }

    @Override
    public void visit( LayerDescriptor layerDescriptor )
            throws InvalidApplicationException
    {
        LayerBean layer = new LayerBean();
        layer.name = layerDescriptor.name();
        for ( LayerDescriptor eachUsedLayer : layerDescriptor.usedLayers().layers() ) {
            layer.usedLayers.add( eachUsedLayer.name() );
        }
        app.layers.add( layer );

        currentLayerBean = layer;
    }

    @Override
    public void visit( ModuleDescriptor moduleDescriptor )
            throws InvalidApplicationException
    {
        ModuleBean module = new ModuleBean();
        module.name = moduleDescriptor.name();
        currentLayerBean.modules.add( module );

        currentModuleBean = module;
    }

    @Override
    public void visit( ServiceDescriptor serviceDescriptor )
            throws InvalidApplicationException
    {
        ServiceBean service = populateComposite( serviceDescriptor, new ServiceBean() );
        service.identity = serviceDescriptor.identity();
        service.configurationType = serviceDescriptor.configurationType();
        service.instanciateOnStartup = serviceDescriptor.isInstantiateOnStartup();
        currentModuleBean.services.add( service );
    }

    @Override
    public void visit( ImportedServiceDescriptor importedServiceDescriptor )
            throws InvalidApplicationException
    {
        ServiceBean service = new ServiceBean();
        service.type = importedServiceDescriptor.type();
        service.visibility = importedServiceDescriptor.visibility().name();
        service.identity = importedServiceDescriptor.identity();
        service.imported = true;
        currentModuleBean.services.add( service );
    }

    @Override
    public void visit( ObjectDescriptor objectDescriptor )
            throws InvalidApplicationException
    {
        currentModuleBean.objects.add( populateObject( objectDescriptor, new ObjectBean() ) );
    }

    @Override
    public void visit( TransientDescriptor transientDescriptor )
            throws InvalidApplicationException
    {
        currentModuleBean.transients.add( populateComposite( transientDescriptor, new TransientBean() ) );
    }

    @Override
    public void visit( ValueDescriptor valueDescriptor )
            throws InvalidApplicationException
    {
        currentModuleBean.values.add( populateComposite( valueDescriptor, new ValueBean() ) );
    }

    @Override
    public void visit( EntityDescriptor entityDescriptor )
            throws InvalidApplicationException
    {
        currentModuleBean.entities.add( populateComposite( entityDescriptor, new EntityBean() ) );
    }

    public ApplicationBean applicationBean()
    {
        return app;
    }

    private <T extends CompositeBean> T populateComposite( AbstractCompositeDescriptor descriptor, T bean )
    {
        populateObject( descriptor, bean );
        for ( Class<?> eachMixinType : descriptor.mixinTypes() ) {
            bean.mixinTypes.add( eachMixinType );
        }
        return bean;
    }

    private <T extends ObjectBean> T populateObject( ObjectDescriptor descriptor, T bean )
    {
        bean.type = descriptor.type();
        bean.visibility = descriptor.visibility().name();
        return bean;
    }

}
