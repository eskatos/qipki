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
package org.codeartisans.qipki.core.services;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

public abstract class AbstractBoxedDomainRepository<T>
        implements BoxedDomainRepository<T>
{

    protected final UnitOfWorkFactory uowf;
    protected final QueryBuilderFactory qbf;

    public AbstractBoxedDomainRepository( UnitOfWorkFactory uowf, QueryBuilderFactory qbf )
    {
        this.uowf = uowf;
        this.qbf = qbf;
    }

    @Override
    public T findByIdentity( String identity )
    {
        return uowf.currentUnitOfWork().get( getBoxedClass(), identity );
    }

    @Override
    public Query<T> findAllPaginated( int firstResult, int maxResults )
    {
        QueryBuilder<T> builder = qbf.newQueryBuilder( getBoxedClass() );
        Query<T> query = builder.newQuery( uowf.currentUnitOfWork() ).
                firstResult( firstResult ).
                maxResults( maxResults );
        return query;
    }

    // TODO make getBoxedClass more robust and cleanup the code
    protected final Class<T> getBoxedClass()
    {
        Class<T> rv = null;
        Type genericSuperClass = this.getClass().getGenericSuperclass();
        Type genericSuperSuperClass = ( ( Class ) genericSuperClass ).getGenericSuperclass();
        if ( genericSuperSuperClass instanceof ParameterizedType ) {
            ParameterizedType parameterizedType = ( ParameterizedType ) genericSuperSuperClass;
            for ( Type actual : parameterizedType.getActualTypeArguments() ) {
                if ( actual instanceof Class ) {
                    if ( rv == null ) {
                        rv = ( Class<T> ) actual;
                    }
                }
            }
        }
        // Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        return rv;
    }

}
