package org.mism.modelgen;

import org.mism.modelgen.api.ObjectUtil;
import org.mism.modelgen.api.HashCode;
import java.math.BigInteger;

public class TestInterfaceObject implements org.mism.modelgen.TestInterface, org.mism.modelgen.api.Cloneable<org.mism.modelgen.TestInterface> {

    String name;
    BigInteger iD;
    OtherTestInterface other;

 
    public TestInterfaceObject () {}


    public void setName(String _name)
    {
       this.name = _name;
    }

    public String getName()
    {
       return this.name;
    }

    public void setID(BigInteger _iD)
    {
       this.iD = _iD;
    }

    public BigInteger getID()
    {
       return this.iD;
    }

    public void setOther(OtherTestInterface _other)
    {
       this.other = _other;
    }

    public OtherTestInterface getOther()
    {
       return this.other;
    }

    public org.mism.modelgen.TestInterface shallowClone() {
        TestInterfaceObject cl = new TestInterfaceObject();
        cl.name = this.name;
        cl.iD = this.iD;
        cl.other = this.other;
        return cl;
    }
    
    public org.mism.modelgen.TestInterface deepClone() {
        TestInterfaceObject cl = new TestInterfaceObject();
        cl.name = this.name;
        cl.iD = this.iD;
        cl.other = (OtherTestInterface) ((org.mism.modelgen.api.Cloneable)this.other).deepClone();
        return cl;
    }

    public boolean equals(Object o) {
        if (o==null) return false;
        else if (this==o) return true;
        else if (o instanceof TestInterfaceObject) {
           TestInterfaceObject other = (TestInterfaceObject) o;
           return
               ObjectUtil.equals(this.name, other.name) &&
               ObjectUtil.equals(this.iD, other.iD) &&
               ObjectUtil.equals(this.other, other.other) &&
           true
           ;    
        }
        else return false;
    }
    
    public int hashCode()
    {
        return ObjectUtil.newHash()
           .hash(this.name)
           .hash(this.iD)
           .hash(this.other)
        .value();
        
    }
}