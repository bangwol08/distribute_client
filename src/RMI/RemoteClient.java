package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteClient
{
    public RemoteInterface getConnect(String host, int port)
    {
        try
        {
            //등록된 서버를 찾기 위해 Registry 객체를 생성한 후 사용할 객체를 불러온다.
            Registry reg = LocateRegistry.getRegistry(host, port);
            // Remote의 객체로 리턴해주기때문에 다운캐스팅해준다.
            RemoteInterface inf = (RemoteInterface) reg.lookup("server");

            return inf;
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
