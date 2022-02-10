package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {
  // Create rule to manage JMock expectations
  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

  // Create mock objects
  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memoryCard = context.mock(MemoryCard.class);

  // Instantiate the camera
  Camera camera = new Camera(sensor, memoryCard);
  WriteListener writeListener = camera;

  // Create mock data that is passed from sensor to memory card when the shutter is pressed while
  // camera is on
  static final byte[] DATA = {20, 10, 30, 5, 25};

  // Test to check that switching the camera on powers up the sensor
  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
          }
        });

    camera.powerOn();
  }

  // Test to check that switching the camera off powers down the sensor
  @Test
  public void switchingTheCameraOffPowersDownTheSensor() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerDown();
          }
        });

    camera.powerOff();
  }

  // Test to check that pressing the shutter when the camera power is off does nothing
  @Test
  public void pressingTheShutterWhenThePowerIsOffDoesNothing() {

    context.checking(
        new Expectations() {
          {
          }
        });

    // Default state of camera is off
    camera.pressShutter();
  }

  // Test to check that data is copied from the sensor to memory card
  // when the shutter is pressed with the camera power on
  @Test
  public void pressingTheShutterWithThePowerOnCopiesDataFromTheSensorToMemoryCard() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(DATA));
            exactly(1).of(memoryCard).write(DATA);
          }
        });

    camera.powerOn();
    camera.pressShutter();
  }

  // Test to check that if data is currently being written, switching the camera off does not power
  // down the sensor
  @Test
  public void switchingTheCameraOffWhileWritingDoesNotPowerDownTheSensor() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(DATA));
            exactly(1).of(memoryCard).write(DATA);
            // no calls to powerDown expected
          }
        });
    camera.powerOn();
    camera.pressShutter();
    camera.powerOff();
  }

  // Test to check that once writing the data has completed, the camera powers down the sensor
  @Test
  public void cameraPowersDownTheSensorOnceWritingHasCompleted() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(DATA));
            exactly(1).of(memoryCard).write(DATA);
            exactly(1).of(sensor).powerDown();
          }
        });
    camera.powerOn();
    camera.pressShutter();
    writeListener.writeComplete();
  }
}
