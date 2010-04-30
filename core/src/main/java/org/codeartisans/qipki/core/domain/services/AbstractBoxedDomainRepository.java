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
package org.codeartisans.qipki.core.domain.services;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

public abstract class AbstractBoxedDomainRepository<T>
        implements BoxedDomainRepository<T>
{

    private UnitOfWorkFactory uowf;
    private QueryBuilderFactory qbf;

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
    private Class<T> getBoxedClass()
    {
        Class<T> rv = null;
        Type genericSuperClass = this.getClass().getGenericSuperclass();
        Type genericSuperSuperClass = ( ( Class ) genericSuperClass ).getGenericSuperclass();
        System.out.println( "genericSuperClass.toString(): " + genericSuperClass.toString() );
        System.out.println( "genericSuperClass.getClass().getName(): " + genericSuperClass.getClass().getName() );
        System.out.println( "genericSuperSuperClass.toString(): " + genericSuperSuperClass.toString() );
        if ( genericSuperSuperClass instanceof ParameterizedType ) {
            ParameterizedType parameterizedType = ( ParameterizedType ) genericSuperSuperClass;
            for ( Type actual : parameterizedType.getActualTypeArguments() ) {
                System.out.println( "actual.toString(): " + actual.toString() );
                if ( actual instanceof Class ) {
                    System.out.println( "((Class<?>) actual).getName(): " + ( ( Class<?> ) actual ).getName() );
                    if ( rv == null ) {
                        rv = ( Class<T> ) actual;
                    }
                } else {
                    System.out.println( "actual.getClass().getName(): " + actual.getClass().getName() );
                }
            }
        }
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        System.out.println( "genericInterfaces.toString(): " + Arrays.toString( genericInterfaces ) );
        return rv;
    }

}
