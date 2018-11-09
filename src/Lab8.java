import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Lab8 {

	private JFrame frame;
	private JTextField txtCurr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Lab8 window = new Lab8();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Lab8() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Exchange Rates");
		lblNewLabel.setBounds(10, 11, 99, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Please insert currency: 1");
		lblNewLabel_1.setBounds(10, 36, 155, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		txtCurr = new JTextField();
		txtCurr.setToolTipText("USD, CNY...");
		txtCurr.setBounds(156, 33, 86, 20);
		frame.getContentPane().add(txtCurr);
		txtCurr.setColumns(10);
		
		JTextArea txtArea1 = new JTextArea();
		txtArea1.setBounds(10, 75, 414, 163);
		frame.getContentPane().add(txtArea1);
		
		JButton btnCheck = new JButton("Check");
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread (new Runnable()
				{	
					public void run()
					{
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("base",txtCurr.getText()));
				
						String strUrl = "https://api.exchangeratesapi.io/latest";
						JSONObject jsnObj = makeHttpRequest(strUrl, "GET", params);
				
						try
						{
							String dateT = jsnObj.getString("date");
							JSONObject rates = (JSONObject) jsnObj.get("rates");
							String USD = rates.get("USD").toString();
							String EUR = rates.get("EUR").toString();
							String CAD = rates.get("CAD").toString();
							String CNY = rates.get("CNY").toString();
							String SGD = rates.get("SGD").toString();
							String MYR = rates.get("MYR").toString();
							String INR = rates.get("INR").toString();
					
							String strSetText = "Date: "+dateT+"\n\nExchange Rates :- "+"\nUSD:\t"+USD+"\nEUR:\t"+EUR+"\nCAD:\t"+CAD+"\nCNY:\t"+CNY+"\nSGD:\t"+SGD+"\nMYR:\t"+MYR+"\nINR:\t"+INR;
					
							txtArea1.setText(strSetText);
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
	
				});
				thread.start();
			}
		});
		btnCheck.setBounds(335, 32, 89, 23);
		frame.getContentPane().add(btnCheck);
	}
	
	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params){
		InputStream is = null;
		String json = "";
		JSONObject jobj = null;
		//Making http request
		
		try{
			// check for rq method
			
			if (method.equalsIgnoreCase("POST")){
				//rq method is post
				//default http client
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			
			}else if (method.equalsIgnoreCase("GET")){
				// rq method is get
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);
				
				HttpResponse htppResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = htppResponse.getEntity();
				is = httpEntity.getContent();
				
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			
			jobj = new JSONObject(json);
			
		}catch(JSONException e ){
			try{
				JSONArray jsnArr = new JSONArray(json);
				jobj = jsnArr.getJSONObject(0);
				
			}catch (JSONException e1){
				e1.printStackTrace();
			}
		}catch (Exception ee) {
			// TODO: handle exception
			ee.printStackTrace();
		}
		return jobj;
	}
}
