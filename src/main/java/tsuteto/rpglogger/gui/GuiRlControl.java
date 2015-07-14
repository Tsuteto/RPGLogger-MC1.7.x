package tsuteto.rpglogger.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.logging.RlLogLine;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.settings.EnumWindowPosition;
import tsuteto.rpglogger.settings.RpgLoggerSettings;

import java.util.Iterator;
import java.util.List;


/**
 * Renders the RPG logger window
 *
 * @author Tsuteto
 *
 */
public class GuiRlControl extends GuiScreen
{
    private final Minecraft mc;
    private List<RlLogLine> logMsgList = RpgLogger.getInstance().logger.logMsgList;
    private List<RlLogLine> displayList = Lists.newArrayList();
    private int scrollPosition;
    private boolean field_146251_k;
    private int prevWidth;

    private GuiButton btnToggleWindow;

    public GuiRlControl(Minecraft p_i1022_1_)
    {
        this.mc = p_i1022_1_;
    }

    public void draw()
    {
        RpgLoggerSettings settings = RpgLogger.getInstance().settings;

        if (mc.gameSettings.showDebugInfo || settings.getLines() == 0)
        {
            return;
        }

        RlLogManager logManager = RpgLogger.getInstance().logger;
        boolean isGuiOpen = this.isWindowOpen();
        double opacity = settings.getOpacity();
        int logSize = settings.getLines();
        int scale = settings.getScale();
        EnumWindowPosition position = isGuiOpen ? EnumWindowPosition.TOP_RIGHT : settings.getPosition();
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int guiScale = scaledresolution.getScaleFactor();
        float logScaleFactor;
        if (scale != 0 && scale < guiScale && !isGuiOpen)
        {
            logScaleFactor = (float) guiScale / (float) scale;
        }
        else
        {
            logScaleFactor = 1;
        }
        int guiW = scaledresolution.getScaledWidth();
        int guiH = scaledresolution.getScaledHeight();
        int width = getWindowWidth();

        if (width != prevWidth)
        {
            this.refreshChat();
        }

        int linesShown = 0;

        FontRenderer fontrenderer = mc.fontRenderer;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glScalef(1.0F / logScaleFactor, 1.0F / logScaleFactor, 1.0F);
        // GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        for (int k2 = 0; k2 + this.scrollPosition < displayList.size() && k2 < logSize; k2++)
        {
            RlLogLine logLine = displayList.get(k2 + this.scrollPosition);
            double d = (logManager.getUpdateTick() - logLine.getCreatedTick()) / 200D;
            d = 1.0D - d;
            d *= 10D;
            if (d < 0.2D)
            {
                d = 0.0D;
            }
            if (d > opacity)
            {
                d = opacity;
            }
            d *= d;
            int k3 = (int) (255D * d);
            if (isGuiOpen) k3 = 255;
            if (k3 > 0)
            {
                String s1 = logLine.message;
                int y;
                if (position == EnumWindowPosition.TOP_LEFT)
                {
                    y = 2;
                }
                else if (position == EnumWindowPosition.TOP_CENTER)
                {
                    y = (int) (guiW * logScaleFactor / 2 - width / 2);
                }
                else
                {
                    y = (int) (guiW * logScaleFactor - width - 2);
                }
                int x = 2 + k2 * 9;
                drawRect(y, x - 1, y + width, x + 8, k3 / 2 << 24);
                GL11.glEnable(GL11.GL_BLEND);
                fontrenderer.drawString(s1, y, x, logLine.color.getRGB() + (k3 << 24));
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                linesShown++;
            }
        }
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //GL11.glDisable(GL11.GL_BLEND);
        //GL11.glEnable(GL11.GL_ALPHA_TEST);

        if (isGuiOpen)
        {
            int j1 = mc.fontRenderer.FONT_HEIGHT;
            int l = logMsgList.size();
            GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
            int entireHeight = l * j1 + l;
            int windowHeight = linesShown * j1 + linesShown;
            int l2 = this.scrollPosition * windowHeight / l;
            int l1 = windowHeight * windowHeight / entireHeight;

            if (entireHeight != windowHeight)
            {
                int i2 = l2 > 0 ? 170 : 96;
                int i3 = this.field_146251_k ? 13382451 : 3355562;
                drawRect(0, -l2, 2, -l2 - l1, i3 + (i2 << 24));
                drawRect(2, -l2, 1, -l2 - l1, 13421772 + (i2 << 24));
            }
        }

        GL11.glPopMatrix();

        this.prevWidth = width;
    }

    public void updateTick()
    {
        RpgLogger.getInstance().logger.onTick();
    }

    public void addToList(RlLogLine chat)
    {
//        if (id != 0)
//        {
//            this.deleteChatLine(id);
//        }

        String s = chat.message;
        int i;
        for (; this.mc.fontRenderer.getStringWidth(s) > getWindowWidth(); s = s.substring(0, i))
        {
            for (i = 1; i < s.length()
                    && this.mc.fontRenderer.getStringWidth(s.substring(0, i + 1)) <= getWindowWidth(); i++)
            {}
            this.addToList(chat.split(s.substring(i)));
        }

        displayList.add(0, chat.split(s));

        while (this.displayList.size() > 500)
        {
            this.displayList.remove(this.displayList.size() - 1);
        }
    }

    public void refreshChat()
    {
        this.displayList.clear();
        this.resetScroll();

        for (int i = this.logMsgList.size() - 1; i >= 0; --i)
        {
            RlLogLine chatline = this.logMsgList.get(i);
            this.addToList(chatline);
        }
    }

    public void resetScroll()
    {
        this.scrollPosition = 0;
        this.field_146251_k = false;
    }

    public void scroll(int p_146229_1_)
    {
        this.scrollPosition += p_146229_1_;
        int j = this.logMsgList.size();

        if (this.scrollPosition > j - this.getWindowMaxLines())
        {
            this.scrollPosition = j - this.getWindowMaxLines();
        }

        if (this.scrollPosition <= 0)
        {
            this.scrollPosition = 0;
            this.field_146251_k = false;
        }
    }

    public int getWindowWidth()
    {
        return isWindowOpen() ? this.width - 220 : RpgLogger.getInstance().settings.getWindowWidth();
    }

    public int getWindowMaxLines()
    {
        return RpgLogger.getInstance().settings.getLines();
    }

    public boolean isWindowOpen()
    {
        return this.mc.currentScreen instanceof GuiRlControl;
    }

    public void deleteChatLine(int p_146242_1_)
    {
        Iterator<RlLogLine> iterator = this.displayList.iterator();
        RlLogLine chatline;

        while (iterator.hasNext())
        {
            chatline = iterator.next();

            if (chatline.getCreatedTick() == p_146242_1_)
            {
                iterator.remove();
            }
        }

        iterator = this.logMsgList.iterator();

        while (iterator.hasNext())
        {
            chatline = iterator.next();

            if (chatline.getCreatedTick() == p_146242_1_)
            {
                iterator.remove();
                break;
            }
        }
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, 30, 30, 150, 20, I18n.format("rpglogger.control.openLog")));
        this.buttonList.add(btnToggleWindow = new GuiButton(1, 30, 55, 150, 20, this.getToggleWindowBtnString()));
        this.buttonList.add(new GuiButton(100, 75, 80, 60, 20, I18n.format("rpglogger.control.close")));
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        // background
        drawRect(5, 5, 200, 130, 0x80 << 24);

        String s;
        s = I18n.format("rpglogger.control.title");
        int fw = mc.fontRenderer.getStringWidth(s);
        mc.fontRenderer.drawStringWithShadow(s, 5 + 100 - fw / 2, 12, 0xffffff);

        s = I18n.format("rpglogger.control.guide");
        fw = mc.fontRenderer.getStringWidth(s);
        mc.fontRenderer.drawStringWithShadow(s, 5 + 100 - fw / 2, 115, 0xcccccc);

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    private String getToggleWindowBtnString()
    {
        boolean isLogWindowEnabled = RpgLogger.getInstance().isWindowEnabled;
        return I18n.format("rpglogger.control.toggleWindow", isLogWindowEnabled ? I18n.format("options.on") : I18n.format("options.off"));
    }

    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 1)
            {
                i = 1;
            }

            if (i < -1)
            {
                i = -1;
            }

            if (!isShiftKeyDown())
            {
                i *= 7;
            }

            RpgLogger.getInstance().logWindowGui.scroll(i);
        }
    }

    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 1)
        {
            this.mc.displayGuiScreen(null);
        }
        else
        {
            if (p_73869_2_ == Keyboard.KEY_PRIOR)
            {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().func_146232_i() - 1);
            }
            else if (p_73869_2_ == Keyboard.KEY_NEXT)
            {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().func_146232_i() + 1);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.id == 0)
        {
            if (RpgLogger.getInstance().logFileWriter != null)
            {
                RpgLogger.getInstance().logFileWriter.showLogFile();
            }
        }
        else if (p_146284_1_.id == 1)
        {
            RpgLogger.getInstance().isWindowEnabled ^= true;
            btnToggleWindow.displayString = this.getToggleWindowBtnString();
        }
        else if (p_146284_1_.id == 100)
        {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void onGuiClosed()
    {
        this.resetScroll();
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
