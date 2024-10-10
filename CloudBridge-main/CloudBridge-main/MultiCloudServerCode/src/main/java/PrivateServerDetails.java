import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.io.IOException;
import java.util.StringJoiner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PrivateServerDetails
 */
public class PrivateServerDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public PrivateServerDetails() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Process details of 10.0.0.33
        String privateServerIp = "10.0.0.33";
        String pingResult = pingServer(privateServerIp);
        String ramUtilization = getRamUtilization();
        String serverDetails = getServerDetails();

        // Build HTML response
        String htmlResponse = buildHtmlResponse(pingResult, ramUtilization, serverDetails);

        // Send the response
        response.setContentType("text/html");
        response.getWriter().write(htmlResponse);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private String pingServer(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            boolean reachable = inet.isReachable(5000); // Timeout = 5000ms
            return reachable ? "Host is reachable" : "Host is NOT reachable";
        } catch (Exception e) {
            return "Error in pinging host: " + e.getMessage();
        }
    }



    private String getRamUtilization() {
        return executeCommand("free -m");
    }

    private String getServerDetails() {
        return executeCommand("uname -a");
    }

    private String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringJoiner sj = new StringJoiner("\n");
            while ((line = reader.readLine()) != null) {
                sj.add(line);
            }
            return sj.toString();
        } catch (IOException e) {
            return "Error executing command: " + e.getMessage();
        }
    }

    private String buildHtmlResponse(String pingResult, String ramUtilization, String serverDetails) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Private Server Details</title>");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"details.css\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h2>Private Server Details</h2>");
        sb.append("<h3>Ping Result:</h3>");
        sb.append("<pre>").append(pingResult).append("</pre>");
        
        sb.append("<h3>RAM Utilization:</h3>");
        sb.append("<pre>").append(ramUtilization).append("</pre>");
        sb.append("<h3>Server Details:</h3>");
        sb.append("<pre>").append(serverDetails).append("</pre>");
        sb.append("<a href=\"index.html\">Back to Form</a>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
}
