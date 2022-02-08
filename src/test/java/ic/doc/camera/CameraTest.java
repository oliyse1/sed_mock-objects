package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class CameraTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();



  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memoryCard = context.mock(MemoryCard.class);

  Camera camera = new Camera(sensor, memoryCard);
  WriteListener writeListener = camera;

  byte DATA[] = {20,10,30,5};


  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
    }});

    camera.powerOn();
  }

  @Test
  public void switchingTheCameraOffPowersDownTheSensor() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerDown();
    }});

    camera.powerOff();
  }

  @Test
  public void pressingTheShutterWhenThePowerIsOffDoesNothing() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerDown();
    }});

    camera.powerOff();
    camera.pressShutter();
  }

  @Test
  public void pressingTheShutterWithThePowerOnCopiesDataFromTheSensorToMemoryCard() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
      exactly(1).of(sensor).readData(); will(returnValue(DATA));
      exactly(1).of(memoryCard).write(DATA);
    }});
    camera.powerOn();
    camera.pressShutter();
  }

  @Test
  public void switchingTheCameraOffWhileWritingDoesNotPowerDownTheSensor() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
      exactly(1).of(sensor).readData(); will(returnValue(DATA));
      exactly(1).of(memoryCard).write(DATA);
    }});
    camera.powerOn();
    camera.pressShutter();
    camera.powerOff();
  }

  @Test
  public void cameraPowersDownTheSensorOnceWritingHasCompleted() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
      exactly(1).of(sensor).readData(); will(returnValue(DATA));
      exactly(1).of(memoryCard).write(DATA);
      exactly(1).of(sensor).powerDown();
    }});
    camera.powerOn();
    camera.pressShutter();
    writeListener.writeComplete();
  }

}
