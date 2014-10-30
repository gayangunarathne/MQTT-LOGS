import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.client.mqttv3.util.Debug;

public class Subscriber {

	static final String BROKER_URL = "tcp://localhost:1883";// Replace
															// with your
															// mqtt
															// broker url
	// static final String BROKER_URL = "tcp://test.mosquitto.org:1883";//public
	// mosquitto server
	static final String TOPIC = "topology/#"; // Change according to your
												// application
	static Debug d;

	public static void main(String args[]) {

		try {
			// Creating new default persistence for mqtt client
			MqttDefaultFilePersistence persistence = new MqttDefaultFilePersistence(
					"/tmp");

			// mqtt client with specific url and a random client id
			MqttClient client = new MqttClient(BROKER_URL, "Subscriber-ID",
					persistence);
			d = client.getDebug();
			client.connect();
			System.out.println("Subscribing to topic '" + TOPIC + "' from "
					+ client.getServerURI());
			// Subscribing to specific topic
			client.subscribe(TOPIC);

			// It will trigger when a new message is arrived
			MqttCallback callback = new MqttCallback() {

				@Override
				public void connectionLost(Throwable arg0) {
					d.dumpClientDebug();
					System.out.println("Connection lost");
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void messageArrived(String arg0, MqttMessage arg1)
						throws Exception {
					System.out.println("Message:"
							+ new String(arg1.getPayload()) + "topic  " + arg0);

				}
			};
			// Continue waiting for messages until the Enter is pressed
			client.setCallback(callback);
			System.out.println("Press <Enter> to exit");
			try {
				System.in.read();
			} catch (IOException e) {
				// If we can't read we'll just exit
			}
			client.disconnect();
			System.out.println("Client Disconnected");

		} catch (MqttException e) {
			d.dumpBaseDebug();
			// e.printStackTrace();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
