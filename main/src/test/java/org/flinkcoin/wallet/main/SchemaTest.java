package org.flinkcoin.wallet.main;

import org.junit.jupiter.api.Test;

public class SchemaTest extends SchemaTestBase {

    @Test
    public void testMigrations() throws Exception {
        compareWithApgDiff();
    }
}
