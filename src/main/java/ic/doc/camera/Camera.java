package ic.doc.camera;

public class Camera implements WriteListener {

  // Field to store whether camera power is on, assume camera is off by default
  private boolean powerOn = false;

  // Field to store whether data is currently being written, assumed no data is currently being
  // written as default
  private boolean isWriting = false;

  // Other class members
  private MemoryCard memoryCard;
  private Sensor sensor;

  // Constructor for Camera class
  public Camera(Sensor sensor, MemoryCard memoryCard) {
    this.sensor = sensor;
    this.memoryCard = memoryCard;
  }

  // Function that mimics behaviour of pressing the shutter on a camera
  public void pressShutter() {
    // If camera power is on, pressing the shutter copies data from the sensor to the memory card
    if (powerOn) {
      byte[] data = sensor.readData();
      memoryCard.write(data);
      // Set isWriting status to true, starts writing
      isWriting = true;
    }
  }

  // Function that turns the power of the camera on
  // The coursework spec does not say what happens to the sensor when a second image is taken
  // without turning the power off and on again after taking the first image, because the sensor
  // powers down after the first image has been written to the memory card
  // Therefore i've assumed that powerOn is called each time before taking a new image to power up
  // the sensor
  public void powerOn() {
    powerOn = true;
    sensor.powerUp();
  }

  // Function that turns the power of the camera off
  public void powerOff() {

    powerOn = false;
    // Turning the camera off only powers down the sensor when data is not currently being written
    if (isWriting == false) {
      sensor.powerDown();
    }
  }

  // Overridden function of the WriteListener interface
  // This function can be called by other components to let the camera know the status of writing
  // data
  // This function sets isWriting status to false and powers down the sensor when writing is
  // completed
  @Override
  public void writeComplete() {

    isWriting = false;
    sensor.powerDown();
  }
}
