/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.el.ValueExpression;
/*     */ import javax.faces.component.UIComponent;
/*     */ import javax.faces.component.UIInput;
/*     */ import javax.faces.component.behavior.ClientBehaviorHolder;
/*     */ import javax.faces.context.FacesContext;
/*     */ import javax.faces.context.ResponseWriter;
/*     */ import javax.faces.convert.Converter;
/*     */ import javax.faces.model.SelectItem;
/*     */ import org.primefaces.component.selectoneradio.SelectOneRadio;
/*     */ import org.primefaces.renderkit.InputRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectOneRadioRenderer
/*     */   extends InputRenderer
/*     */ {
/*     */   public void decode(FacesContext context, UIComponent component) {
/*  30 */     SelectOneRadio radio = (SelectOneRadio)component;
/*     */     
/*  32 */     if (radio.isDisabled()) {
/*     */       return;
/*     */     }
/*     */     
/*  36 */     decodeBehaviors(context, (UIComponent)radio);
/*     */     
/*  38 */     String clientId = radio.getClientId(context);
/*  39 */     String value = (String)context.getExternalContext().getRequestParameterMap().get(clientId);
/*     */     
/*  41 */     radio.setSubmittedValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
/*  46 */     SelectOneRadio radio = (SelectOneRadio)component;
/*     */     
/*  48 */     encodeMarkup(context, radio);
/*  49 */     encodeScript(context, radio);
/*     */   }
/*     */   
/*     */   protected void encodeMarkup(FacesContext context, SelectOneRadio radio) throws IOException {
/*  53 */     ResponseWriter writer = context.getResponseWriter();
/*  54 */     String clientId = radio.getClientId(context);
/*  55 */     String style = radio.getStyle();
/*  56 */     String styleClass = radio.getStyleClass();
/*  57 */     styleClass = (styleClass == null) ? "ui-selectoneradio ui-widget" : ("ui-selectoneradio ui-widget " + styleClass);
/*     */     
/*  59 */     writer.startElement("table", (UIComponent)radio);
/*  60 */     writer.writeAttribute("id", clientId, "id");
/*  61 */     writer.writeAttribute("class", styleClass, "styleClass");
/*  62 */     if (style != null) {
/*  63 */       writer.writeAttribute("style", style, "style");
/*     */     }
/*  65 */     encodeSelectItems(context, radio);
/*     */     
/*  67 */     writer.endElement("table");
/*     */   }
/*     */   
/*     */   protected void encodeScript(FacesContext context, SelectOneRadio radio) throws IOException {
/*  71 */     ResponseWriter writer = context.getResponseWriter();
/*  72 */     String clientId = radio.getClientId(context);
/*     */     
/*  74 */     writer.startElement("script", null);
/*  75 */     writer.writeAttribute("type", "text/javascript", null);
/*     */     
/*  77 */     writer.write(radio.resolveWidgetVar() + " = new PrimeFaces.widget.SelectOneRadio({id:'" + clientId + "'");
/*     */     
/*  79 */     if (radio.isDisabled()) writer.write(",disabled: true");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     encodeClientBehaviors(context, (ClientBehaviorHolder)radio);
/*     */     
/*  86 */     writer.write("});");
/*     */     
/*  88 */     writer.endElement("script");
/*     */   }
/*     */   
/*     */   protected void encodeOptionInput(FacesContext context, SelectOneRadio radio, String clientId, String containerClientId, boolean checked, boolean disabled, String label, String value) throws IOException {
/*  92 */     ResponseWriter writer = context.getResponseWriter();
/*     */     
/*  94 */     writer.startElement("div", null);
/*  95 */     writer.writeAttribute("class", "ui-radiobutton-inputwrapper", null);
/*     */     
/*  97 */     writer.startElement("input", null);
/*  98 */     writer.writeAttribute("id", containerClientId, null);
/*  99 */     writer.writeAttribute("name", clientId, null);
/* 100 */     writer.writeAttribute("type", "radio", null);
/* 101 */     writer.writeAttribute("value", value, null);
/*     */     
/* 103 */     if (checked) writer.writeAttribute("checked", "checked", null); 
/* 104 */     if (disabled) writer.writeAttribute("disabled", "disabled", null); 
/* 105 */     if (radio.getOnchange() != null) writer.writeAttribute("onchange", radio.getOnchange(), null);
/*     */     
/* 107 */     writer.endElement("input");
/*     */     
/* 109 */     writer.endElement("div");
/*     */   }
/*     */   
/*     */   protected void encodeOptionLabel(FacesContext context, SelectOneRadio radio, String containerClientId, String label) throws IOException {
/* 113 */     ResponseWriter writer = context.getResponseWriter();
/*     */     
/* 115 */     writer.startElement("label", null);
/* 116 */     writer.writeAttribute("for", containerClientId, null);
/* 117 */     writer.write(label);
/* 118 */     writer.endElement("label");
/*     */   }
/*     */   
/*     */   protected void encodeOptionOutput(FacesContext context, SelectOneRadio radio, boolean checked) throws IOException {
/* 122 */     ResponseWriter writer = context.getResponseWriter();
/* 123 */     String styleClass = "ui-radiobutton-box ui-widget ui-corner-all ui-radiobutton-relative ui-state-default";
/* 124 */     styleClass = checked ? (styleClass + " ui-state-active") : styleClass;
/*     */     
/* 126 */     String iconClass = "ui-radiobutton-icon";
/* 127 */     iconClass = checked ? (iconClass + " ui-icon ui-icon-bullet") : iconClass;
/*     */     
/* 129 */     writer.startElement("div", null);
/* 130 */     writer.writeAttribute("class", styleClass, null);
/*     */     
/* 132 */     writer.startElement("span", null);
/* 133 */     writer.writeAttribute("class", iconClass, null);
/* 134 */     writer.endElement("span");
/*     */     
/* 136 */     writer.endElement("div");
/*     */   }
/*     */   
/*     */   protected void encodeSelectItems(FacesContext context, SelectOneRadio radio) throws IOException {
/* 140 */     ResponseWriter writer = context.getResponseWriter();
/* 141 */     List<SelectItem> selectItems = getSelectItems(context, (UIInput)radio);
/* 142 */     String layout = radio.getLayout();
/* 143 */     boolean pageDirection = (layout != null && layout.equals("pageDirection"));
/*     */     
/* 145 */     for (SelectItem selectItem : selectItems) {
/* 146 */       Object itemValue = selectItem.getValue();
/* 147 */       String itemLabel = selectItem.getLabel();
/*     */       
/* 149 */       if (pageDirection) {
/* 150 */         writer.startElement("tr", null);
/*     */       }
/*     */       
/* 153 */       encodeOption(context, radio, itemLabel, itemValue);
/*     */       
/* 155 */       if (pageDirection) {
/* 156 */         writer.endElement("tr");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void encodeOption(FacesContext context, SelectOneRadio radio, String itemLabel, Object itemValue) throws IOException {
/* 162 */     ResponseWriter writer = context.getResponseWriter();
/* 163 */     Object value = radio.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     Converter converter = null;
/*     */     
/* 170 */     String convertedValue = getOptionAsString(context, (UIComponent)radio, converter, itemValue);
/* 171 */     String clientId = radio.getClientId(context);
/* 172 */     String containerClientId = radio.getContainerClientId(context);
/* 173 */     boolean disabled = radio.isDisabled();
/* 174 */     Class type = getValueType(context, (UIInput)radio);
/*     */     
/* 176 */     if (itemValue != null && !itemValue.equals("")) {
/* 177 */       itemValue = context.getApplication().getExpressionFactory().coerceToType(itemValue, type);
/*     */     }
/*     */     
/* 180 */     boolean checked = (value != null && value.equals(itemValue));
/*     */     
/* 182 */     writer.startElement("td", null);
/*     */     
/* 184 */     String styleClass = "ui-radiobutton ui-widget";
/* 185 */     if (disabled) {
/* 186 */       styleClass = styleClass + " ui-state-disabled";
/*     */     }
/*     */     
/* 189 */     writer.startElement("div", null);
/* 190 */     writer.writeAttribute("class", styleClass, null);
/*     */     
/* 192 */     encodeOptionInput(context, radio, clientId, containerClientId, checked, disabled, itemLabel, convertedValue);
/* 193 */     encodeOptionOutput(context, radio, checked);
/*     */     
/* 195 */     writer.endElement("div");
/* 196 */     writer.endElement("td");
/*     */     
/* 198 */     writer.startElement("td", null);
/* 199 */     encodeOptionLabel(context, radio, containerClientId, itemLabel);
/* 200 */     writer.endElement("td");
/*     */   }
/*     */   
/*     */   protected Class getValueType(FacesContext context, UIInput input) {
/* 204 */     ValueExpression ve = input.getValueExpression("value");
/* 205 */     Class type = (ve == null) ? String.class : ve.getType(context.getELContext());
/*     */     
/* 207 */     return (type == null) ? String.class : type;
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\SelectOneRadioRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */