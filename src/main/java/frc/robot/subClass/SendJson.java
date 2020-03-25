package frc.robot.subClass;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SendJson {

    private Timer loopTimer, sendTimer;
    private double totalLoopTime;
    private int loopCount;
    private double preBatteryVoltage, batteryVoltage;
    private boolean is_SendFinished;
    private Map<Double, Double> voltageChangeTime;
    private Map<Double, State.ControlMode> modeChangeTime;
    private State.ControlMode preControlMode;

    private Thread thread;


    public SendJson() {
        loopTimer = new Timer();
        sendTimer = new Timer();
        voltageChangeTime = new HashMap<>();
        thread = new Thread(() -> { callPost(makeJson()); });
        thread.setDaemon(true);
        preControlMode = State.ControlMode.m_Drive;
        is_SendFinished = true;
    }

    public void sendJsonInit() {
        sendTimer.reset();
        sendTimer.start();
    }

    public void timerStart(){
        loopTimer.reset();
        loopTimer.start();
    }

    public void finalProcess(State.ControlMode controlMode) {
        if(is_SendFinished) {
            modeCheck(controlMode);
            batterCheck();
            if(sendTimer.get() > 2.0) {
                is_SendFinished = false;
                thread.start();
            }
            loopTimeMeasure(loopTimer.get());
        }
    }

    private void modeCheck(State.ControlMode controlMode) {
        if(preControlMode != controlMode) {
            modeChangeTime.put(sendTimer.get(), controlMode);
        }
        preControlMode = controlMode;
    }

    private void batterCheck() {
        batteryVoltage = RobotController.getBatteryVoltage();
        if(preBatteryVoltage > batteryVoltage) {
            voltageChangeTime.put(sendTimer.get(), batteryVoltage);
        }
        preBatteryVoltage = batteryVoltage;
    }

    private void loopTimeMeasure(double loopTime) {
        totalLoopTime += loopTime;
        return;
    }

    private String makeJson() {
        String json = "";
        json += "{";

        if(loopCount != 0) {
            json += jsonFormat("平均ループ時間", Double.toString(totalLoopTime / loopCount));
        } else {
            json += jsonFormat("平均ループ時間", "N/A");
        }

            json += ", \"モード変更履歴 \" : {";
            for(Map.Entry<Double, State.ControlMode> entry : modeChangeTime.entrySet()) {
                json += jsonFormat(Double.toStrientry.getKey()), entry.getValue().toString() + ", ");
            }
            json += "}";

            json += ", \"電圧変化履歴 \" : {";
            for(Map.Entry<Double, Double> entry : voltageChangeTime.entrySet()) {
                json += jsonFormat(Double.toString(entry.getKey()), Double.toString(entry.getValue()) + ", ");
            }
            json += "}";

        json += "}";
        return json;
    }

    private String jsonFormat(String dataName, String data) {
        return " \" " + dataName + " \" : " + data;
    }

    /**
     * JSON文字列の送信
     *
     * @param strPostUrl 送信先URL
     * @param JSON 送信するJSON文字列
     * @return
     */

    private String strPostUrl = "https://api.sakura-tempesta.org/robot/";

    public String callPost(String JSON) {

        HttpURLConnection con = null;
        StringBuffer result = new StringBuffer();
        try {

            URL url = new URL(strPostUrl);
            con = (HttpURLConnection) url.openConnection();
            // HTTPリクエストコード
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "jp");
            // データがJSONであること、エンコードを指定する
            con.setRequestProperty("Content-Type", "application/JSON; charset=utf-8");
            // POSTデータの長さを設定
            con.setRequestProperty("Content-Length", String.valueOf(JSON.length()));
            // リクエストのbodyにJSON文字列を書き込む
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(JSON);
            out.flush();
            con.connect();

            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 通信に成功した
                // テキストを取得する
                final InputStream in = con.getInputStream();
                String encoding = con.getContentEncoding();
                if (null == encoding) {
                    encoding = "UTF-8";
                }
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                // 1行ずつテキストを読み込む
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();
            } else {
                outputStatus("HTTP_BAD");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            outputStatus(e1.toString());
        } finally {
            if (con != null) {
                // コネクションを切断
                con.disconnect();
            }
        }

        sendTimer.reset();
        sendTimer.start();
        is_SendFinished = true;
        return result.toString();
    }

    private void outputStatus(String text) {
        SmartDashboard.putString("SendJsonStatus", text);
    }

    private void outputResult(String text) {
        SmartDashboard.putString("SendjsonResult", text);
    }
}