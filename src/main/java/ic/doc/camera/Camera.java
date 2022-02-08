package ic.doc.camera;

public class Camera implements WriteListener {
  //assume camera is off by default
  private boolean powerOn = false;
  private MemoryCard memoryCard;
  private Sensor sensor;
  //assumed no data is currently being written as default
  private boolean isWriting = false;

  Camera(Sensor sensor, MemoryCard memoryCard) {
    this.sensor = sensor;
    this.memoryCard = memoryCard;
  }
  public void pressShutter() {
    if(powerOn) {
      byte[] data = sensor.readData();
      memoryCard.write(data);
      isWriting = true;
    }
  }

  public void powerOn() {
    powerOn = true;
    sensor.powerUp();
  }

  public void powerOff() {

    powerOn = false;
    if (isWriting == false) {
      sensor.powerDown();
    }
  }

  @Override
  public void writeComplete() {
    isWriting = false;
    sensor.powerDown();
  }
}

