const mqttSub = require('mqtt');
const readlineSub = require('readline');

// Create readline interface for user input
const rlSub = readlineSub.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Prompt user for MQTT host and topic information
rlSub.question('Enter MQTT broker host (e.g., mqtt://localhost:1883): ', (host) => {
  host = host || 'mqtt://localhost:1883';
  rlSub.question('Enter topic (default: test/topic): ', (topic) => {
    topic = topic || 'test/topic';

    // Connect to MQTT broker with provided host
    const client = mqttSub.connect(host);

    client.on('connect', () => {
      console.log('Connected to MQTT broker at', host);
      
      // Subscribe to a topic
      client.subscribe(topic, (err) => {
        if (!err) {
          console.log(`Subscribed to topic: ${topic}`);
        } else {
          console.error('Subscription error:', err);
        }
      });
    });

    client.on('message', (topic, message) => {
      console.log(`Received message from ${topic}: ${message.toString()}`);
    });

    client.on('error', (error) => {
      console.error('MQTT client error:', error);
    });

    client.on('close', () => {
      console.log('Connection closed');
    });

    // Handle program exit
    rlSub.on('close', () => {
      console.log('Closing MQTT connection...');
      client.end();
      process.exit();
    });

    process.on('SIGINT', () => {
      console.log('Closing MQTT connection...');
      client.end();
      process.exit();
    });
  });
});
