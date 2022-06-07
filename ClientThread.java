package space.shades.myapplication;

import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ClientThread extends Thread
{
    private InetAddress ip;
    private DatagramSocket socket;
    private MainActivity.DataHandler dataHandler;
    private int p_sq = -1;
    static int sq = 0;

    static int iMap = 0;
    static float fExplosion = 0.0f;
    static float fDefuse = 0.0f;
    static int iPlantedC4posX = 0;
    static int iPlantedC4posY = 0;
    static char cBombSite = 'C';
    static int iUnplantedC4posX = 0;
    static int iUnplantedC4posY = 0;
    static int iUnplantedC4posZ = 0;
    static char bBombHasOwner = '0';
    private static char bHasGrenadesChunk = '0';
    static final List<Grenade> vGrenades = Collections.synchronizedList(new ArrayList<Grenade>());
    static final List<Path> vPaths = Collections.synchronizedList(new ArrayList<Path>());
    private static char bHasPlayersChunk = '0';
    static final List<Player> vPlayers = Collections.synchronizedList(new ArrayList<Player>());
    private static char bHasKitsChunk = '0';
    static final List<Kit> vKits = Collections.synchronizedList(new ArrayList<Kit>());
    private static char bHasDropChunk = '0';
    static final List<Drop> vDrop = Collections.synchronizedList(new ArrayList<Drop>());

    static int csgo = 0;
    static final List<Player> vPlayers2 = Collections.synchronizedList(new ArrayList<Player>());

    ClientThread(String sip, MainActivity.DataHandler dataHandler)
    {
        super();
        try
        {
            this.dataHandler = dataHandler;
            ip = InetAddress.getByName("192.168.1." + sip);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            // connect
            socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(new byte[1], 1, ip, 17117);
            socket.send(packet);

            // receive
            byte[] pMsg = new byte[1024];
            while (true)
            {
                packet = new DatagramPacket(pMsg, pMsg.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if (msg.length() > 0) //
                { //
                    if (msg.charAt(0) == '?')
                    {
                        csgo = 1;
                        String[] data_chunks = msg.split(Pattern.quote("|"));
                        vPlayers2.clear();
                        for (String s : data_chunks)
                        {
                            String[] single_player_data = s.split(Pattern.quote("_"));
                            if (single_player_data.length == 2) // x, y
                            {
                                int sgPosX = Integer.parseInt(single_player_data[0]);
                                int sgPosY = Integer.parseInt(single_player_data[1]);

                                vPlayers2.add(new Player("", sgPosX, sgPosY, 1, '0', '0', '0', 0, 0));
                            }
                        }
                    }
                    else
                    {
                        //                                                  nted bomb chunk, unplanted bomb chunk, bHasNadesChunk, bHasPlayersChunk, bHasKitsChunk, {KitsChunk}
                        String[] data_chunks = msg.split(Pattern.quote("#"));  //nted bomb chunk, unplanted bomb chunk, bHasNadesChunk, bHasPlayersChunk, {PlayersChunk}, bHasKitsChunk, {KitsChunk}                                                           inext
                        if (data_chunks.length >= 8) // sequence number, map id, planted bomb chunk, unplanted bomb chunk, bHasNadesChunk, {NadesChunk}, bHasPlayersChunk, {PlayersChunk}, bHasKitsChunk, {KitsChunk}, bHasDropChunk, {DropChunk}
                        {                            //           0        1           2                   3                     4              5              6              7               8                9            10             11
                            sq = Integer.parseInt(data_chunks[0]);
                            if (p_sq >= sq)
                            {
                                msg = msg + " [prev>>]";
                                vPaths.clear(); // if dont then wont clear ?
                            } else
                            {
                                for (int i = 0; i < vPaths.size(); ++i)
                                {
                                    if (vPaths.get(i).sq_num + 25 < sq)
                                    {
                                        vPaths.remove(i);
                                    }
                                }
                                iMap = Integer.parseInt(data_chunks[1]);
                                String[] planted_bomb_chunk = data_chunks[2].split(Pattern.quote("|"));
                                if (planted_bomb_chunk.length == 5) // exp, def, x, y, cBombSite
                                {
                                    fExplosion = Float.parseFloat(planted_bomb_chunk[0]);
                                    fDefuse = Float.parseFloat(planted_bomb_chunk[1]);
                                    iPlantedC4posX = Integer.parseInt(planted_bomb_chunk[2]);
                                    iPlantedC4posY = Integer.parseInt(planted_bomb_chunk[3]);
                                    cBombSite = planted_bomb_chunk[4].charAt(0);
                                }
                                String[] unplanted_bomb_chunk = data_chunks[3].split(Pattern.quote("|"));
                                if (unplanted_bomb_chunk.length == 4) // x, y, z, bHasOwner
                                {
                                    iUnplantedC4posX = Integer.parseInt(unplanted_bomb_chunk[0]);
                                    iUnplantedC4posY = Integer.parseInt(unplanted_bomb_chunk[1]);
                                    iUnplantedC4posZ = Integer.parseInt(unplanted_bomb_chunk[2]);
                                    bBombHasOwner = unplanted_bomb_chunk[3].charAt(0); // was 2 but i think a bug ?
                                }
                                int indexChunk = 4;
                                vGrenades.clear();
                                bHasGrenadesChunk = data_chunks[indexChunk++].charAt(0);
                                if (bHasGrenadesChunk == '1')
                                {
                                    String[] grenades_chunk = data_chunks[indexChunk++].split(Pattern.quote("|"));
                                    for (String s : grenades_chunk)
                                    {
                                        String[] single_grenade_data = s.split(Pattern.quote("_"));
                                        if (single_grenade_data.length == 4) // type, x, y, bDrawSmoke
                                        {
                                            char sgType = single_grenade_data[0].charAt(0);
                                            int sgPosX = Integer.parseInt(single_grenade_data[1]);
                                            int sgPosY = Integer.parseInt(single_grenade_data[2]);
                                            char sgDrawSmoke = single_grenade_data[3].charAt(0);
                                            vGrenades.add(new Grenade(sgType, sgPosX, sgPosY, sgDrawSmoke));
                                            vPaths.add(new Path(sgType, sgPosX, sgPosY, sq));
                                        }
                                    }
                                }
                                vPlayers.clear();
                                vKits.clear(); // doing here because may also get kits from players
                                bHasPlayersChunk = data_chunks[indexChunk++].charAt(0);
                                if (bHasPlayersChunk == '1')
                                {
                                    String[] players_chunk = data_chunks[indexChunk++].split(Pattern.quote("|"));
                                    for (String s : players_chunk)
                                    {
                                        String[] single_player_data = s.split(Pattern.quote("_"));
                                        if (single_player_data.length == 7) // name, x, y, hp, [ bIsEnemy, bIsVulnerable, bHasDefuser ], angle, money
                                        {
                                            String spName = single_player_data[0];
                                            int spPosX = Integer.parseInt(single_player_data[1]);
                                            int spPosY = Integer.parseInt(single_player_data[2]);
                                            int spHealth = Integer.parseInt(single_player_data[3]);
                                            char spIsEnemy = single_player_data[4].charAt(0);
                                            char spIsVulnerable = single_player_data[4].charAt(1);
                                            char spHasDefuser = single_player_data[4].charAt(2);
                                            int spAngle = Integer.parseInt(single_player_data[5]);
                                            int spMoney = Integer.parseInt(single_player_data[6]);
                                            vPlayers.add(new Player(spName, spPosX, spPosY, spHealth, spIsEnemy, spIsVulnerable, spHasDefuser, spAngle, spMoney));
                                            if (spHasDefuser == '1')
                                            {
                                                vKits.add(new Kit(spPosX, spPosY));
                                            }
                                        }
                                    }
                                }
                                bHasKitsChunk = data_chunks[indexChunk++].charAt(0);
                                if (bHasKitsChunk == '1')
                                {
                                    String[] kits_chunk = data_chunks[indexChunk++].split(Pattern.quote("|"));
                                    for (String s : kits_chunk)
                                    {
                                        String[] single_kit_data = s.split(Pattern.quote("_"));
                                        if (single_kit_data.length == 2) // x, y
                                        {
                                            int skPosX = Integer.parseInt(single_kit_data[0]);
                                            int skPosY = Integer.parseInt(single_kit_data[1]);
                                            vKits.add(new Kit(skPosX, skPosY));
                                        }
                                    }
                                }
                                vDrop.clear();
                                bHasDropChunk = data_chunks[indexChunk++].charAt(0);
                                if (bHasDropChunk == '1')
                                {
                                    String[] drop_chunk = data_chunks[indexChunk].split(Pattern.quote("|"));
                                    for (String s : drop_chunk)
                                    {
                                        String[] single_drop_data = s.split(Pattern.quote("_"));
                                        if (single_drop_data.length == 3) // x, y, type
                                        {
                                            int sdPosX = Integer.parseInt(single_drop_data[0]);
                                            int sdPosY = Integer.parseInt(single_drop_data[1]);
                                            char sdType = single_drop_data[2].charAt(0);
                                            vDrop.add(new Drop(sdPosX, sdPosY, sdType));
                                        }
                                    }
                                }
                            }
                            p_sq = sq;
                        }
                    }
                    //dataHandler.sendMessage(Message.obtain(dataHandler, MainActivity.DataHandler.UPDATE_MSG, msg));
                }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(socket != null){
                socket.close();
            }
        }

    }
}


/*

import java.util.regex.Pattern;

public class Main
{
	public static void main(String[] args) {
	    String msg = "4009|0.1|0.0";
	    String[] parts_data = msg.split(Pattern.quote("|"));
	    System.out.println(parts_data.length);
	    for (int i = 0; i < parts_data.length; ++i)
		    System.out.println('[' + parts_data[i] + ']');
	}
}

 */