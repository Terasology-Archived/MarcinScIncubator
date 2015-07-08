/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.html;

import org.terasology.asset.Assets;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.widgets.CursorUpdateEventListener;
import org.terasology.rendering.nui.widgets.UIText;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLFontResolver;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLParseException;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLParser;
import org.terasology.rendering.nui.widgets.browser.data.html.basic.DefaultHTMLDocumentBuilderFactory;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserWidget;

public class HTMLTester extends CoreScreenLayer {
    private BrowserWidget browserWidget;
    private UIText textArea;
    private UIText status;

    private HTMLParser parser = new HTMLParser(new DefaultHTMLDocumentBuilderFactory(
            new HTMLFontResolver() {
                @Override
                public Font getFont(String name, boolean bold, boolean italic) {
                    if (name == null) {
                        return null;
                    }
                    if (bold && italic) {
                        return Assets.getFont(name + "-BoldItalic").get();
                    } else if (bold) {
                        return Assets.getFont(name + "-Bold").get();
                    } else if (italic) {
                        return Assets.getFont(name + "-Italic").get();
                    } else {
                        return Assets.getFont(name + "-Regular").get();
                    }
                }
            }
    ));

    @Override
    protected void initialise() {
        browserWidget = find("browser", BrowserWidget.class);
        textArea = find("textArea", UIText.class);
        status = find("status", UIText.class);

        textArea.subscribe(
                new CursorUpdateEventListener() {
                    @Override
                    public void onCursorUpdated(int oldPosition, int newPosition) {
                        try {
                            DocumentData documentData = parser.parseHTMLDocument(textArea.getText());
                            browserWidget.navigateTo(documentData);
                            status.setText("OK");
                        } catch (HTMLParseException exp) {
                            exp.printStackTrace();
                            status.setText("Error - " + exp.getMessage());
                        }
                    }
                }
        );
    }
}
