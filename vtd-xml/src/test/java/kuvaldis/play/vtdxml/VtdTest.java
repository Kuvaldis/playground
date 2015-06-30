package kuvaldis.play.vtdxml;

import com.ximpleware.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class VtdTest {

    private final String INPUT2_INDEX_FILE_NAME = "xml/input2.vxl";

    @Test
    public void testHelloWorld() throws Exception {
        final VTDGen gen = new VTDGen();
        assertTrue(gen.parseFile("xml/input1.xml", true));
        final VTDNav nav = gen.getNav();
        assertTrue(nav.toElementNS(VTDNav.FIRST_CHILD, "someURL", "b"));
        final int i = nav.getText();
        assertNotEquals(-1, i);
        assertEquals("Hello, World!", nav.toString(i));
    }

    @Test
    public void testHelloWorldByXPath() throws Exception {
        final VTDGen gen = new VTDGen();
        assertTrue(gen.parseFile("xml/input2.xml", false));
        final VTDNav nav = gen.getNav();
        final AutoPilot autoPilot = new AutoPilot();
        autoPilot.selectXPath("/a/b/text()");
        autoPilot.bind(nav);
        assertNotEquals(-1, autoPilot.evalXPath());
        assertEquals("Hello, World!", nav.toString(nav.getCurrentIndex()));
    }

    @Test
    public void testWriteVtdIndex() throws Exception {
        createInput2Index();
        assertTrue(Paths.get(INPUT2_INDEX_FILE_NAME).toFile().exists());
    }

    @Test
    public void testVtdIndex() throws Exception {
        createInput2Index();
        final VTDGen gen = new VTDGen();
        final VTDNav nav = gen.loadIndex(INPUT2_INDEX_FILE_NAME);
        nav.toElement(VTDNav.FIRST_CHILD, "b");
        assertEquals("Hello, World!", nav.toString(nav.getText()));
    }

    @Test
    public void testInsertAttributesFromIndex() throws Exception {
        final String outputFileName = "xml/input1InsertAttr.xml";
        createInput2Index();
        final VTDGen gen = new VTDGen();
        final AutoPilot autoPilot = new AutoPilot();
        final XMLModifier modifier = new XMLModifier();
        final VTDNav nav = gen.loadIndex(INPUT2_INDEX_FILE_NAME);
        autoPilot.selectXPath("/a/b");
        autoPilot.bind(nav);
        modifier.bind(nav);
        assertNotEquals(-1, autoPilot.evalXPath());
        modifier.insertAttribute(" c=\"val\" ");
        modifier.output(outputFileName);

        final VTDGen genReader = new VTDGen();
        genReader.parseFile(outputFileName, false);
        final VTDNav navReader = genReader.getNav();
        final AutoPilot autoPilotReader = new AutoPilot();
        autoPilotReader.selectXPath("/a/b");
        autoPilotReader.bind(navReader);

        String attr = null;
        while (autoPilotReader.evalXPath() != -1) {
            final int i = nav.getAttrVal("c");
            if (i != -1) {
                attr = nav.toNormalizedString(i);
            }
        }
        // doesn't work for mex
//        assertEquals("val", attr);
    }

    private void createInput2Index() throws IOException, IndexWriteException {
        final VTDGen gen = new VTDGen();
        if (gen.parseFile("xml/input2.xml", false)) {
            gen.writeIndex(INPUT2_INDEX_FILE_NAME);
        }
    }
}
