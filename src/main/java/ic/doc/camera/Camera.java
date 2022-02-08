package ic.doc.camera;

public class Camera {
  private boolean powerOn = false;
  private MemoryCard memoryCard;
  private Sensor sensor;

  Camera(Sensor sensor, MemoryCard memoryCard) {
    this.sensor = sensor;
    this.memoryCard = memoryCard;
  }
  public void pressShutter() {
    if(powerOn) {
      byte[] data = sensor.readData();
      memoryCard.write(data);
    }
  }

  public void powerOn() {
    powerOn = true;
    sensor.powerUp();
  }

  public void powerOff() {
    powerOn = false;
    sensor.powerDown();
  }
}

