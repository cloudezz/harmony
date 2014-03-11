
package com.cloudezz.management.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * This a helper class for SSH connectivity.
 */
public class SSHConnection
{
    private Connection connection;
    private SCPClient scpcClient;

    /**
     * SSHConnection authenticate using a user and and a public key
     * @param hostname  The host to connect to
     * @param username  The user name
     * @param keyfile  The certificate public key file
     * @throws IOException
     */
    public SSHConnection(String hostname, String username, File keyfile) throws IOException {
		String keyfilePass = ""; // will be ignored if not needed

        /* Create a connection instance */
        connection = new Connection(hostname);

        /* Now connect */
        connection.connect();

        /* Authenticate */
        boolean isAuthenticated = connection.authenticateWithPublicKey(username, keyfile, keyfilePass);

        if (isAuthenticated == false)
            throw new IOException("Authentication failed.");
    }

    private SCPClient getSCPClient() throws IOException {
        if (scpcClient == null)
        {
            scpcClient = new SCPClient(connection);
        }
        return scpcClient;
    }

    /**
     * This method upload a file via SCPC
     * @param data  The file data
     * @param remoteFileName     The remote file name
     * @param remoteTargetDirectory The remote file directory
     * @throws IOException
     */
    public void upload(byte[] data, String remoteFileName, String remoteTargetDirectory) throws IOException {
        getSCPClient().put(remoteFileName, data.length, remoteTargetDirectory, "0666");
    }

    /**
     * This method execute a command via SSH
     * @param cmd  The command to execute
     * @return  Return the outout of the command as a List of Strings
     * @throws IOException
     */
    public List<String> execCommand(String cmd) throws IOException {

        /* Create a session */
        Session session = connection.openSession();

        List<String>output = new ArrayList<String>();
        try
        {
            session.requestDumbPTY();
            session.execCommand(cmd);

            InputStream stdout = new StreamGobbler(session.getStdout());
            InputStream stderr = new StreamGobbler(session.getStderr());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true)
            {
                String line = br.readLine();
                if (line == null)
                    break;
                output.add(line);
                System.out.println("o>" + line);
            }
            br = new BufferedReader(new InputStreamReader(stderr));
            while (true)
            {
                String line = br.readLine();
                if (line == null)
                    break;
                output.add(line);
                System.out.println("e>" + line);
            }
        } finally
        {
            session.close();
        }

        return output;
    }

    /**
     * This method closes the SSH connections
     */
    public void close() {
         /* Close the connection */
        if (connection != null) {
            connection.close();
            connection = null;
        }

        if (scpcClient != null) {
            scpcClient = null;
        }
    }

}
