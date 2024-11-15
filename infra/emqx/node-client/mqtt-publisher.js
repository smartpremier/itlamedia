const mqtt = require('mqtt');
const readline = require('readline');

// Create readline interface for user input
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Prompt user for MQTT host and topic information
rl.question('Enter MQTT broker host (e.g., mqtt://localhost:1883): ', (host) => {
  host = host || 'mqtt://localhost:1883';
  rl.question('Enter topic (default: test/topic): ', (topic) => {
    topic = topic || 'test/topic';

    // Connect to MQTT broker with provided host
    const client = mqtt.connect(host);
    let messageCount = 0;

    client.on('connect', () => {
      console.log('Connected to MQTT broker at', host);
      // Start publishing messages
      sendMessages();
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
        client.publish(topic, message, { qos: 2 }, (err) => {
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
});
