#for creating the java classes from the pel json file
import json
import os

#creates the enum classes for each of the power loads in the json file
file = open("pel.json", "r")
data = json.load(file)
path = "src/main/java/genericpowersystem/models/power"
powerList = data["power_loads"]
for item in powerList:
    fName = item["name"] + "_State"
    filepath = os.path.join(path, fName + ".java")
    newF = open(filepath, "w")
    stateList = []
    loadList = []
    for ent in item["power_states"]:
        stateList.append(ent["state"].upper())
        loadList.append(ent["power_usage"]["value"])
    body = f"""package genericpowersystem.models.power;
public enum {fName} {{
"""
    for num in range(len(stateList)):
        if num != (len(stateList) - 1):
            state = stateList[num].upper()
            body = body + "\t" + state + "(" + str(loadList[num]) + ")" + ",\n"
        else:
            state = stateList[num].upper()
            body = body + "\t" + state + "(" + str(loadList[num]) + ");"+ "\n"
    newF.write(body)
    constructor = f"""    private final double load;
    {fName}(double load) {{
        this.load = load;
    }}
    public double getLoad() {{
        return load;
    }}
}}
    """
    newF.write(constructor)
    newF.close()


#making a Pel model class that contains an instance of the power load classes and this class interacts with the Battery class
pelPath = os.path.join(path, "PELModel.java")
pelFile = open(pelPath, "w")
initial = f"""package genericpowersystem.models.power;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;

public class PELModel {{
"""
construct = f"""    public PELModel() {{
"""
register = f"""    public void registerStates(Registrar registrar) {{
"""
for x in range(len(powerList)):
    name = powerList[x]["name"]
    initial = initial + "\tpublic SettableState<" + name + "_State> " + name.lower() + "State;\n"

    stateName = powerList[x]["power_states"][0]["state"].upper()
    construct = construct + "\t\tthis." + name.lower() + "State = SettableState.builder(" + name + "_State.class).initialValue(" + name + "_State." + stateName + ").build();\n"

    register = register + "\t\tregistrar.discrete(\"" + name.lower() + "State\"," + name.lower() + "State, new EnumValueMapper<>(" + name + "_State.class));\n"

    if x == (len(powerList) - 1):
        construct = construct + "\t}\n"
        register = register + "\t}\n}"
pelFile.write(initial)
pelFile.write(construct)
pelFile.write(register)
pelFile.close()



                
                
            
                        
        
