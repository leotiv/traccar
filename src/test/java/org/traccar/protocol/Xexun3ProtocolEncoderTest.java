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
        command.setType(Command.TYPE_POWER_OFF);
        verifyEncode(channel, command,
                binary("FC000F03210101234567890123456F663D310248CF"));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_POSITION_PERIODIC);
        command.set(Command.KEY_FREQUENCY, 150);
        verifyEncode(channel, command,
                binary("FC00200321010123456789012345747261636B696E675F73656E643D3135302C31353055C9CF"));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_REBOOT_DEVICE);
        verifyEncode(channel, command,
                binary("FC0010032101012345678901234572657365742C7ACF"));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_CUSTOM);
        command.set(Command.KEY_DATA, "tracking_send=10,10");
        verifyEncode(channel, command,
                binary("FC001E0321010123456789012345747261636B696E675F73656E643D31302C31300F05CF"));
    }
}
