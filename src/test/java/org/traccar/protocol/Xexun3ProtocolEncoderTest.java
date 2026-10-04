package org.traccar.protocol;

import org.junit.jupiter.api.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Command;
import org.traccar.model.Device;

import static org.mockito.Mockito.when;

public class Xexun3ProtocolEncoderTest extends ProtocolTest {

    @Test
    public void testEncode() throws Exception {

        var encoder = inject(new Xexun3ProtocolEncoder(null));
        var channel = channel(encoder);
        var device = encoder.getCacheManager().getObject(Device.class, 1);
        when(device.getUniqueId()).thenReturn("862596080926201");

        Command command;

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_CUSTOM);
        command.set(Command.KEY_DATA, "tk=2,20,0,600,1");
        verifyEncode(channel, command,
                binary("FC001A0321010862596080926201746B3D322C32302C302C3630302C31B0FBCF"));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_REBOOT_DEVICE);
        verifyEncode(channel, command,
                binary("FC000F032101086259608092620172743D31E1BFCF"));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_POSITION_PERIODIC);
        command.set(Command.KEY_FREQUENCY, 150);
        verifyEncode(channel, command,
                binary("FC001B0321010862596080926201746B3D322C3135302C302C3135302C31A661CF"));
    }

}
