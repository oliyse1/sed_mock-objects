package ic.doc.camera;

public class Camera {

  private Sensor sensor;

  Camera(Sensor sensor) {
    this.sensor = sensor;
  }
  public void pressShutter() {
    // not implemented
  }

  public void powerOn() {
    sensor.powerUp();
  }

  public void powerOff() {
    // not implemented
  }
}

