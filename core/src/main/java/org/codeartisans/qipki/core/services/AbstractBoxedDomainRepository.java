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
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        return rv;
    }

}
