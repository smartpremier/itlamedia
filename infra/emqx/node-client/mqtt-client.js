const mqtt = require('mqtt');
const client = mqtt.connect('mqtt://localhost:1883');

const topic = 'test/topic';
let messageCount = 0;

// 연결 이벤트
client.on('connect', () => {
    console.log('Connected to MQTT broker');
    
    // 토픽 구독
    client.subscribe(topic, (err) => {
        if (!err) {
            console.log(`Subscribed to ${topic}`);
            
            // 메시지 발행 시작
            sendMessages();
        }
    });
});

// 메시지 수신 이벤트
client.on('message', (topic, message) => {
    console.log(`Received message on ${topic}: ${message.toString()}`);
});

// 에러 이벤트
client.on('error', (err) => {
    console.error('Connection error:', err);
});

// 연결 끊김 이벤트
client.on('close', () => {
    console.log('Connection closed');
});

// 메시지 발행 함수
function sendMessages() {
    setInterval(() => {
        messageCount++;
        const message = `Test message ${messageCount}`;
        client.publish(topic, message, { qos: 1 }, (err) => {
            if (!err) {
                console.log(`Published: ${message}`);
            }
        });
    }, 2000); // 2초마다 메시지 발행
}

// 프로그램 종료 처리
process.on('SIGINT', () => {
    console.log('Closing MQTT connection...');
    client.end();
    process.exit();
});
