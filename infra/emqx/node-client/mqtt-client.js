const mqtt = require('mqtt');
const readline = require('readline');

// Create readline interface for user input
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Prompt user for MQTT host information
rl.question('Enter MQTT broker host (e.g., mqtt://localhost:1883): ', (host) => {
  host = host || 'mqtt://localhost:1883';
  

  // Connect to MQTT broker with provided host
  const client = mqtt.connect(host);
  const topic = 'test/topic';
  let messageCount = 0;

  client.on('connect', () => {
    console.log('Connected to MQTT broker at', host);
    
    // Subscribe to a topic
    client.subscribe(topic, (err) => {
      if (!err) {
        console.log(`Subscribed to topic: ${topic}`);
        
        // Start publishing messages
        sendMessages();
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

  // Function to publish messages periodically
  function sendMessages() {
    setInterval(() => {
      messageCount++;
      const message = `Test message ${messageCount}`;
      client.publish(topic, message, { qos: 1 }, (err) => {
        if (!err) {
          console.log(`Published: ${message}`);
        }
      });
    }, 2000); // Publish every 2 seconds
  }

  // Handle program exit
  rl.on('close', () => {
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
