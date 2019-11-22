package org.fhi360.lamis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

@Slf4j
public class BaseTest {
    @Test
    public void testPadding() {
        LOG.info("Hospital number: {}", StringUtils.leftPad("B3S4", 7, "0"));
    }
}
