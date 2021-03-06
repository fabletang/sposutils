/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package apache.http.impl.conn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSession;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

/**
 * @since 4.3
 */
@NotThreadSafe
class CPoolProxy implements ManagedHttpClientConnection, HttpContext {

    private volatile apache.http.impl.conn.CPoolEntry poolEntry;

    CPoolProxy(final apache.http.impl.conn.CPoolEntry entry) {
        super();
        this.poolEntry = entry;
    }

    apache.http.impl.conn.CPoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    apache.http.impl.conn.CPoolEntry detach() {
        final apache.http.impl.conn.CPoolEntry local = this.poolEntry;
        this.poolEntry = null;
        return local;
    }

    ManagedHttpClientConnection getConnection() {
        final apache.http.impl.conn.CPoolEntry local = this.poolEntry;
        if (local == null) {
            return null;
        }
        return local.getConnection();
    }

    ManagedHttpClientConnection getValidConnection() {
        final ManagedHttpClientConnection conn = getConnection();
        if (conn == null) {
            throw new ConnectionShutdownException();
        }
        return conn;
    }

    public void close() throws IOException {
        final apache.http.impl.conn.CPoolEntry local = this.poolEntry;
        if (local != null) {
            local.closeConnection();
        }
    }

    public void shutdown() throws IOException {
        final apache.http.impl.conn.CPoolEntry local = this.poolEntry;
        if (local != null) {
            local.shutdownConnection();
        }
    }

    public boolean isOpen() {
        final apache.http.impl.conn.CPoolEntry local = this.poolEntry;
        if (local != null) {
            return !local.isClosed();
        } else {
            return false;
        }
    }

    public boolean isStale() {
        final HttpClientConnection conn = getConnection();
        if (conn != null) {
            return conn.isStale();
        } else {
            return true;
        }
    }

    public void setSocketTimeout(final int timeout) {
        getValidConnection().setSocketTimeout(timeout);
    }

    public int getSocketTimeout() {
        return getValidConnection().getSocketTimeout();
    }

    public String getId() {
        return getValidConnection().getId();
    }

    public void bind(final Socket socket) throws IOException {
        getValidConnection().bind(socket);
    }

    public Socket getSocket() {
        return getValidConnection().getSocket();
    }

    public SSLSession getSSLSession() {
        return getValidConnection().getSSLSession();
    }

    public boolean isResponseAvailable(final int timeout) throws IOException {
        return getValidConnection().isResponseAvailable(timeout);
    }

    public void sendRequestHeader(final HttpRequest request) throws HttpException, IOException {
        getValidConnection().sendRequestHeader(request);
    }

    public void sendRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        getValidConnection().sendRequestEntity(request);
    }

    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        return getValidConnection().receiveResponseHeader();
    }

    public void receiveResponseEntity(final HttpResponse response) throws HttpException, IOException {
        getValidConnection().receiveResponseEntity(response);
    }

    public void flush() throws IOException {
        getValidConnection().flush();
    }

    public HttpConnectionMetrics getMetrics() {
        return getValidConnection().getMetrics();
    }

    public InetAddress getLocalAddress() {
        return getValidConnection().getLocalAddress();
    }

    public int getLocalPort() {
        return getValidConnection().getLocalPort();
    }

    public InetAddress getRemoteAddress() {
        return getValidConnection().getRemoteAddress();
    }

    public int getRemotePort() {
        return getValidConnection().getRemotePort();
    }

    public Object getAttribute(final String id) {
        final ManagedHttpClientConnection conn = getValidConnection();
        if (conn instanceof HttpContext) {
            return ((HttpContext) conn).getAttribute(id);
        } else {
            return null;
        }
    }

    public void setAttribute(final String id, final Object obj) {
        final ManagedHttpClientConnection conn = getValidConnection();
        if (conn instanceof HttpContext) {
            ((HttpContext) conn).setAttribute(id, obj);
        }
    }

    public Object removeAttribute(final String id) {
        final ManagedHttpClientConnection conn = getValidConnection();
        if (conn instanceof HttpContext) {
            return ((HttpContext) conn).removeAttribute(id);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CPoolProxy{");
        final ManagedHttpClientConnection conn = getConnection();
        if (conn != null) {
            sb.append(conn);
        } else {
            sb.append("detached");
        }
        sb.append('}');
        return sb.toString();
    }

    public static HttpClientConnection newProxy(final apache.http.impl.conn.CPoolEntry poolEntry) {
        return new CPoolProxy(poolEntry);
    }

    private static CPoolProxy getProxy(final HttpClientConnection conn) {
        if (!CPoolProxy.class.isInstance(conn)) {
            throw new IllegalStateException("Unexpected connection proxy class: " + conn.getClass());
        }
        return CPoolProxy.class.cast(conn);
    }

    public static apache.http.impl.conn.CPoolEntry getPoolEntry(final HttpClientConnection proxy) {
        final apache.http.impl.conn.CPoolEntry entry = getProxy(proxy).getPoolEntry();
        if (entry == null) {
            throw new ConnectionShutdownException();
        }
        return entry;
    }

    public static apache.http.impl.conn.CPoolEntry detach(final HttpClientConnection conn) {
        return getProxy(conn).detach();
    }

}
