package simulation.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import simulation.Model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConfigReader {
    public static SimulationData parse(File file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            ArrayList<Factory> factories = new ArrayList<>();
            NodeList nList = doc.getElementsByTagName("metadonnees").item(0).getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    List<ItemStack> inputItems;
                    List<ItemStack> outputItems = null;

                    String factoryType = element.getAttribute("type");
                    int productionTime = 0;
                    if (factoryType.equals("entrepot")) {
                        inputItems = readNode(element, "entree", new String[] {"type", "capacite"});
                    }
                    else {
                        inputItems = readNode(element, "entree", new String[] {"type", "quantite"});
                        outputItems = readNode(element, "sortie", new String[] {"type", "quantite"});
                        productionTime = ((ItemStack) readNode(element, "interval-production", new String[] {}).get(0)).getQuantity();
                    }
                    NodeList attributeIcone = element.getElementsByTagName("icones").item(0).getChildNodes();
                    List<Pair> iconList = readNode((Element) attributeIcone, "icone", new String[] {"type", "path"});

                    Factory f;
                    if (outputItems != null) {
                        f = new ElementFactory(factoryType, iconList, inputItems, outputItems, productionTime);
                    }
                    else {
                        f = new Warehouse(factoryType, iconList, inputItems);
                    }
                    factories.add(f);
                }
            }

            SimulationData simulationData = new SimulationData(factories);
            nList = doc.getElementsByTagName("simulation").item(0).getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    if (element.getTagName().equals("usine")) {
                        String type = element.getAttribute("type");
                        int id = Integer.parseInt(element.getAttribute("id"));
                        int x = Integer.parseInt(element.getAttribute("x"));
                        int y = Integer.parseInt(element.getAttribute("y"));
                        simulationData.addFactory(type, id, x, y);
                    }
                    else if (element.getTagName().equals("chemins")) {
                        for (int j = 0; j < element.getChildNodes().getLength(); j++) {
                            Node node = element.getChildNodes().item(j);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element cheminElement = (Element) node;
                                int from = Integer.parseInt(cheminElement.getAttribute("de"));
                                int to = Integer.parseInt(cheminElement.getAttribute("vers"));

                                SimulationData.SimulationElement elementFrom = simulationData.getElementById(from);
                                if (elementFrom == null) {
                                    throw new IllegalArgumentException("Error while loading routes - source unknown: " + from);
                                }
                                SimulationData.SimulationElement elementTo = simulationData.getElementById(to);
                                if (elementTo == null) {
                                    throw new IllegalArgumentException("Error while loading routes - destination unknown: " + to);
                                }
                                simulationData.addRoute(elementFrom, elementTo);
                            }
                        }
                    }
                }
            }

            setObservers(simulationData.getSimulationElements());
            return simulationData;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List readNode(Element document, String tag, String[] attributeIdentifiers) {
        ArrayList attributes = new ArrayList<>();
        NodeList nList = document.getElementsByTagName(tag);
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (attributeIdentifiers.length > 0) {
                    String itemType = "";
                    for (String attributeIdentifier : attributeIdentifiers) {
                        switch (attributeIdentifier) {
                            case "type":
                                itemType = element.getAttribute(attributeIdentifier);
                                break;
                            case "quantite":
                            case "capacite":
                                String quantityString = element.getAttribute(attributeIdentifier);
                                int itemQuantity = 1;
                                if (!quantityString.equals("")) {
                                    itemQuantity = Integer.parseInt(element.getAttribute(attributeIdentifier));
                                }
                                ItemStack itemStack = new ItemStack(new Item(itemType), itemQuantity);
                                attributes.add(itemStack);
                                break;
                            case "path":
                                String path = element.getAttribute(attributeIdentifier);
                                attributes.add(new Pair(itemType, path));
                                break;
                        }
                    }
                }
                else {
                    ItemStack itemStack = new ItemStack(new Item(tag), Integer.parseInt(element.getTextContent()));
                    attributes.add(itemStack);
                }
            }
        }
        return attributes;
    }

    private static void setObservers(ArrayList<SimulationData.SimulationElement> simulationElements) {
        ArrayList<Warehouse> warehouses = new ArrayList<>();
        ArrayList<ElementFactory> elementFactories = new ArrayList<>();
        for (SimulationData.SimulationElement simulationElement : simulationElements) {
            Factory factory = simulationElement.factory;
            if (factory instanceof Warehouse) {
                warehouses.add((Warehouse) factory);
            }
            if (factory instanceof ElementFactory) {
                elementFactories.add((ElementFactory) factory);
            }
        }

        for (Warehouse warehouse : warehouses) {
            for (ElementFactory elementFactory : elementFactories) {
                warehouse.attach(elementFactory);
            }
        }
    }
}
