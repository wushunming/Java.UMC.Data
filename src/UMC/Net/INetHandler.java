package UMC.Net;

import java.io.IOException;

public interface INetHandler {

    void processRequest(NetContext context) throws IOException;
}
