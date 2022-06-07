package space.shades.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class TestView extends View {
    Context context;

    public static MapInfo m_empty = new MapInfo(0.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
    public static MapInfo m_dust2 = new MapInfo(-2239.0f, 1846.0f, -1076.0f, 3264.0f, 3.782f, 3.760f);
    public static MapInfo m_tuscan = new MapInfo(-2322.0f, 1570.0f, -2336.0f, 2256.0f, 3.6037f, 3.5931f);

    public static MapInfo m_season = new MapInfo(-829.0f, 3775.0f, -2484.0f, 2200.0f, 4.2629f, 4.2389f);
    public static MapInfo m_cache = new MapInfo(-1770.0f, 3328.0f, -1282.0f, 2278.0f, 4.7203f, 4.7403f);
    public static MapInfo m_nuke = new MapInfo(-3013.0f, 3692.0f, -4155.0f, 968.0f, 6.2055f, 6.2781f);
    public static MapInfo m_inferno = new MapInfo(-1985.0f, 2703.0f, -785.0f, 3660.0f, 4.3407f, 4.4141f);
    public static MapInfo m_strike = new MapInfo(-2684.0f, 1490.0f, -2552.0f, 912.0f, 3.8648f, 3.8747f);
    public static MapInfo m_frost = new MapInfo(-1340.0f, 2266.0f, -42.0f, 3836.0f, 3.3388f, 3.3663f);
    public static MapInfo m_train = new MapInfo(-2038.0f, 2082.0f, -1798.0f, 1902.0f, 3.8148f, 3.8144f);
    public static MapInfo m_losttemple = new MapInfo(-1456.0f, 2712.0f, -3102.0f, 1360.0f, 3.8592f, 3.8732f);
    public static MapInfo m_cbble = new MapInfo(-3456.0f, 1536.0f, -2304.0f, 2944.0f, 4.6222f, 4.6278f);
    public static MapInfo m_contra = new MapInfo(-2278.0f, 2278.0f, -2559.0f, 1672.0f, 4.2185f, 4.2565f);
    public static MapInfo m_plane2d = new MapInfo(0.0f, 0.0f, 0.0f, 0.0f, 3.8f, 0.0f);
    public static MapInfo m_current = m_empty;
    private int iMapCurrent = 0;
    ImageView imageViewMain;
    CheckBox cbSettingNames;
    CheckBox cbSettingPaths;
    CheckBox cbSettingRadius;
    CheckBox cbSettingTrace;

    Paint paintYellow;
    Paint paintBlue;
    Paint paintRed;
    Paint paintGreen;
    Paint paintGray;
    Paint paintWhite;
    Paint paintWhite2;
    Paint paintOrange;

    private static final int verticesColors[] = {
            Color.WHITE, Color.WHITE, Color.WHITE, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF
    };

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;

        paintYellow = new Paint();
        paintYellow.setColor(Color.YELLOW);
        paintYellow.setStrokeWidth(5);

        paintBlue = new Paint();
        paintBlue.setColor(Color.CYAN);
        paintBlue.setStrokeWidth(5);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStrokeWidth(5);
        paintRed.setTextSize(25.0f);

        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setStrokeWidth(5);
        paintGreen.setTextSize(25.0f);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setStrokeWidth(5);
        paintWhite.setTextSize(50.0f);

        paintWhite2 = new Paint();
        paintWhite2.setARGB(75, 255, 255, 255);
        paintWhite2.setStrokeWidth(3);

        paintOrange = new Paint();
        paintOrange.setARGB(75, 255, 128, 0);
        paintOrange.setStrokeWidth(5);

        paintGray = new Paint();
        paintGray.setARGB(180, 35, 35, 35);
        paintGray.setStrokeWidth(5);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (ClientThread.csgo > 0)
        {
            ChangeMapByID(99);

            synchronized (ClientThread.vPlayers2)
            {
                for (int i = 0; i < ClientThread.vPlayers2.size(); ++i)
                {

                    float cx = toScreenX(ClientThread.vPlayers2.get(i).x);
                    float cy = toScreenY(ClientThread.vPlayers2.get(i).y);

                    canvas.drawCircle(cx, cy, 12.0f, paintRed);
                }
            }

        }
        else if (imageViewMain != null && cbSettingNames != null && cbSettingPaths != null && cbSettingRadius != null && cbSettingTrace != null)
        {
            if (ClientThread.iMap != iMapCurrent)
            {
                ChangeMapByID(ClientThread.iMap);
            }
            // was planted or cs_ map
            boolean bUnplantedBombDoesntExist = (
                    ClientThread.iUnplantedC4posX == 0 &&
                            ClientThread.iUnplantedC4posY == 0 &&
                            ClientThread.iUnplantedC4posZ == 0 && ClientThread.bBombHasOwner == '0'
            );
            if (bUnplantedBombDoesntExist)
            {
                if (ClientThread.fExplosion > 0)
                {
                    canvas.drawRect(0, 1153, ClientThread.fExplosion, 1252, paintYellow);
                    if (ClientThread.fDefuse < ClientThread.fExplosion)
                        canvas.drawRect(0, 1253, ClientThread.fDefuse, 1352, paintBlue);
                    else
                        canvas.drawRect(0, 1253, ClientThread.fDefuse, 1352, paintRed);
                }
                if ((ClientThread.iPlantedC4posX != 0 && ClientThread.iPlantedC4posY != 0) || ClientThread.fExplosion > 0)
                {
                    float cx = toScreenX(ClientThread.iPlantedC4posX);
                    float cy = toScreenY(ClientThread.iPlantedC4posY);
                    canvas.drawLine(cx - 17.0f, cy - 17.0f, cx + 17.0f, cy + 17.0f, paintRed);
                    canvas.drawLine(cx - 17.0f, cy + 17.0f, cx + 17.0f, cy - 17.0f, paintRed);

                    if (ClientThread.cBombSite == 'A')
                        canvas.drawText("bomb: A", 800, 1400, paintRed);
                    else if (ClientThread.cBombSite == 'B')
                        canvas.drawText("bomb: B", 800, 1400, paintRed);
                }
            }
            else
            {
                float cx = toScreenX(ClientThread.iUnplantedC4posX);
                float cy = toScreenY(ClientThread.iUnplantedC4posY);
                canvas.drawLine(cx - 17.0f, cy - 17.0f, cx + 17.0f, cy + 17.0f, paintRed);
                canvas.drawLine(cx - 17.0f, cy + 17.0f, cx + 17.0f, cy - 17.0f, paintRed);
            }
            //dropped guns
            synchronized (ClientThread.vDrop)
            {
                for (int i = 0; i < ClientThread.vDrop.size(); ++i)
                {
                    float cx = toScreenX(ClientThread.vDrop.get(i).x);
                    float cy = toScreenY(ClientThread.vDrop.get(i).y);
                    char cType = ClientThread.vDrop.get(i).type;
                    if (cType == '1') // primary
                        canvas.drawCircle(cx, cy, 8.0f, paintRed);
                    else if (cType == '2') // pistol
                        canvas.drawCircle(cx, cy, 8.0f, paintYellow);
                    else // nades
                        canvas.drawCircle(cx, cy, 8.0f, paintWhite);
                }
            }
            //paths
            if (m_current != m_plane2d && cbSettingPaths.isChecked()) // when turning in 2d paths are useless
            {
                synchronized (ClientThread.vPaths)
                {
                    for (int i = 0; i < ClientThread.vPaths.size(); ++i)
                    {
                        float cx = toScreenX(ClientThread.vPaths.get(i).x);
                        float cy = toScreenY(ClientThread.vPaths.get(i).y);
                        char cType = ClientThread.vPaths.get(i).type;
                        if (cType == 'f')
                        {
                            canvas.drawCircle(cx, cy, 3.0f, paintBlue);
                        } else if (cType == 'h')
                        {
                            canvas.drawCircle(cx, cy, 3.0f, paintYellow);
                        } else if (cType == 's')
                        {
                            canvas.drawCircle(cx, cy, 3.0f, paintGreen);
                        } else // cType == 'o'ther
                        {
                            canvas.drawCircle(cx, cy, 3.0f, paintWhite);
                        }
                    }
                }
            }
            //ArrayList<Grenade> sGrenades = ClientThread.vGrenades;
            synchronized (ClientThread.vGrenades)
            {
                for (int i = 0; i < ClientThread.vGrenades.size(); ++i)
                {
                    float cx = toScreenX(ClientThread.vGrenades.get(i).x);
                    float cy = toScreenY(ClientThread.vGrenades.get(i).y);
                    char cType = ClientThread.vGrenades.get(i).type;
                    char cbDrawSmoke = ClientThread.vGrenades.get(i).bDrawSmoke;
                    if (cType == 'f')
                    {
                        if (cbSettingRadius.isChecked())
                            canvas.drawCircle(cx, cy, 675.0f / m_current.mx, paintWhite2);
                        canvas.drawLine(cx - 10.0f, cy - 10.0f, cx + 10.0f, cy + 10.0f, paintBlue);
                        canvas.drawLine(cx - 10.0f, cy + 10.0f, cx + 10.0f, cy - 10.0f, paintBlue);
                    }
                    else if (cType == 'h')
                    {
                        if (cbSettingRadius.isChecked())
                            canvas.drawCircle(cx, cy, 270.0f / m_current.mx, paintOrange);
                        canvas.drawLine(cx - 10.0f, cy - 10.0f, cx + 10.0f, cy + 10.0f, paintYellow);
                        canvas.drawLine(cx - 10.0f, cy + 10.0f, cx + 10.0f, cy - 10.0f, paintYellow);
                    }
                    else if (cType == 's')
                    {
                        if (cbDrawSmoke == '0')
                        {
                            canvas.drawLine(cx - 10.0f, cy - 10.0f, cx + 10.0f, cy + 10.0f, paintGreen);
                            canvas.drawLine(cx - 10.0f, cy + 10.0f, cx + 10.0f, cy - 10.0f, paintGreen);
                        }
                        else
                        {
                            canvas.drawCircle(cx, cy, 140.0f / m_current.mx, paintGray);
                        }
                    }
                    else // cType == 'o'ther
                    {
                        canvas.drawLine(cx - 10.0f, cy - 10.0f, cx + 10.0f, cy + 10.0f, paintWhite);
                        canvas.drawLine(cx - 10.0f, cy + 10.0f, cx + 10.0f, cy - 10.0f, paintWhite);
                    }
                }
            }
            //ArrayList<Player> sPlayers = ClientThread.vPlayers;
            synchronized (ClientThread.vPlayers)
            {
                int enemies_cnt = 0;
                for (int i = 0; i < ClientThread.vPlayers.size(); ++i)
                {
                    String cName = ClientThread.vPlayers.get(i).name;
                    float cx = toScreenX(ClientThread.vPlayers.get(i).x);
                    float cy = toScreenY(ClientThread.vPlayers.get(i).y);
                    int cHealth = ClientThread.vPlayers.get(i).hp;
                    char cIsEnemy = ClientThread.vPlayers.get(i).bIsEnemy;
                    char cIsVulnerable = ClientThread.vPlayers.get(i).bIsVulnerable;
                    int cMoney = ClientThread.vPlayers.get(i).money;

                    if (cIsEnemy == '1')
                    {
                        canvas.drawCircle(cx, cy, 12.0f, paintRed);
                        if (cbSettingNames.isChecked())
                            canvas.drawText(cName, cx - 6.0f * cName.length(), cy + 60.0f, paintRed); // name

                        if (enemies_cnt < 6)
                        {
                            while (cName.length() < 8)
                                cName += ' ';
                            canvas.drawText(cName, 1, 1400 + enemies_cnt * 60, paintGreen);
                            canvas.drawText(Integer.toString(cMoney), 150, 1400 + enemies_cnt * 60, paintGreen);
                            ++enemies_cnt;
                        }
                    }
                    else if (cIsEnemy == '0')
                    {
                        canvas.drawCircle(cx, cy, 12.0f, paintGreen);
                        if (cbSettingNames.isChecked())
                            canvas.drawText(cName, cx - 6.0f * cName.length(), cy + 60.0f, paintGreen); // name
                    }
                    else // cIsEnemy == '2' (LocalPlayer)
                    {
                        canvas.drawCircle(cx, cy, 12.0f, paintWhite);
                    }
                    if (cIsVulnerable == '1')
                    {
                        canvas.drawLine(cx - 10.0f, cy - 10.0f, cx + 10.0f, cy + 10.0f, paintGray);
                        canvas.drawLine(cx - 10.0f, cy + 10.0f, cx + 10.0f, cy - 10.0f, paintGray);
                    }

                    canvas.drawRect(cx - 24, cy + 20, cx + 25, cy + 27, paintGray);
                    canvas.drawRect(cx - 24, cy + 20, cx - 25 + cHealth * 0.5f, cy + 27, paintGreen);
                }

                for (int i = 0; i < ClientThread.vPlayers.size(); ++i)
                {
                    float nx = ClientThread.vPlayers.get(i).x;
                    float ny = ClientThread.vPlayers.get(i).y;
                    char cIsEnemy = ClientThread.vPlayers.get(i).bIsEnemy;
                    int angle = ClientThread.vPlayers.get(i).angle;

                    if (m_current != m_plane2d)
                    {
                        float tx = (float) Math.cos((360.0f - angle) * 0.01745);
                        float ty = (float) Math.sin((180.0f - angle) * 0.01745);
                        if (cIsEnemy == '2' && cbSettingTrace.isChecked()) // LocalPlayer
                        {
                            canvas.drawLine(toScreenX(nx), toScreenY(ny), toScreenX(nx + tx * 2500.0f), toScreenY(ny + ty * 2500.0f), paintWhite2);
                        }
                        float ax = toScreenX(nx + tx * 190.0f);
                        float ay = toScreenY(ny + ty * 190.0f);
                        float lx = toScreenX(nx + (float) Math.cos((360.0f - angle + 45.0f) * 0.01745) * 160.0f);
                        float ly = toScreenY(ny + (float) Math.sin((180.0f - angle + 45.0f) * 0.01745) * 160.0f);
                        float rx = toScreenX(nx + (float) Math.cos((360.0f - angle - 45.0f) * 0.01745) * 160.0f);
                        float ry = toScreenY(ny + (float) Math.sin((180.0f - angle - 45.0f) * 0.01745) * 160.0f);


                        float verts[] = {
                                lx, ly, ax, ay, rx, ry
                        };

                        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, verts.length, verts, 0, null, 0, verticesColors,   0, null, 0, 0, paintWhite);

                        //canvas.drawLine(cx, cy, ax, ay, paintWhite);
                    }
                }
            }
            // kits
            synchronized (ClientThread.vKits)
            {
                for (int i = 0; i < ClientThread.vKits.size(); ++i)
                {
                    float cx = toScreenX(ClientThread.vKits.get(i).x);
                    float cy = toScreenY(ClientThread.vKits.get(i).y);

                    canvas.drawCircle(cx, cy, 4.0f, paintBlue);
                }
            }
        }
        else
        {
            imageViewMain = ((MainActivity) context).findViewById(R.id.eMainBackground);
            cbSettingNames = ((MainActivity) context).findViewById(R.id.checkBoxSettingNames);
            cbSettingPaths = ((MainActivity) context).findViewById(R.id.checkBoxSettingPaths);
            cbSettingRadius = ((MainActivity) context).findViewById(R.id.checkBoxSettingRadius);
            cbSettingTrace = ((MainActivity) context).findViewById(R.id.checkBoxSettingTrace);
        }
        invalidate();
    }

    private float toScreenX(float x)
    {
        float new_x = x;
        if (m_current != m_plane2d)
        {
            if (new_x < m_current.lb)
                new_x = m_current.lb;
            if (new_x > m_current.rb)
                new_x = m_current.rb;

            //EditText mmx = ((MainActivity) context).findViewById(R.id.editTextMX);

            //new_x = (new_x - m_current.lb) / Float.valueOf(mmx.getText().toString());

            new_x = (new_x - m_current.lb) / m_current.mx;

            if (new_x < 0.0f)
                new_x *= -1.0f;
        }
        return new_x;
    }

    private float toScreenY(float y)
    {
        float new_y = y;
        if (m_current != m_plane2d)
        {
            if (new_y < m_current.bb)
                new_y = m_current.bb;
            if (new_y > m_current.tb)
                new_y = m_current.tb;

            //EditText mmy = ((MainActivity) context).findViewById(R.id.editTextMY);

            //new_y = (new_y - m_current.tb) / Float.valueOf(mmy.getText().toString());

            new_y = (new_y - m_current.tb) / m_current.my;

            if (new_y < 0.0f)
                new_y *= -1.0f;
        }
        return new_y;
    }

    private void ChangeMapByID(int iMap)
    {
        if (iMap == 0) {
            imageViewMain.setImageResource(R.drawable.dark_background);
            m_current = m_empty;
        } 
	/*
	..
	*/
	else if (iMap == 12) {
            imageViewMain.setImageResource(R.drawable.contra_android);
            m_current = m_contra;
        } else if (iMap == 99) {
            imageViewMain.setImageResource(R.drawable.plane2d_android);
            m_current = m_plane2d;
        }

        iMapCurrent = iMap;
    }
}