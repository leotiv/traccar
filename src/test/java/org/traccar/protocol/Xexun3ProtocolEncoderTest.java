package org.traccar.protocol;

import org.junit.jupiter.api.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Command;

public class Xexun3ProtocolEncoderTest extends ProtocolTest {

    @Test
    public void testEncode() throws Exception {

        var channel = channel(inject(new Xexun3ProtocolEncoder(null)));

        Command command;

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_CUSTOM);
        command.set(Command.KEY_DATA, "tracking_send=10,10");
        verifyEncode(channel, command,
                binary("FC001E0321010123456789012345747261636B696E675F73656E643D31302C31300F05CF"));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_REBOOT_DEVICE);
        verifyEncode(channel, command,
                binary("FC000F032101012345678901234572743D310D6ACF"));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_POSITION_PERIODIC);
        command.set(Command.KEY_FREQUENCY, 150);
        verifyEncode(channel, command,
                binary("FC001B0321010123456789012345746B3D322C3135302C302C3135302C31B810CF"));
    }

}
