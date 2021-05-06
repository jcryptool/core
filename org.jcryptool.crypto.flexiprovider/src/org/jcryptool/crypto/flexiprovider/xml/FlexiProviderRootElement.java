// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jcryptool.crypto.flexiprovider.descriptors.meta.MetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.MetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.MetaLength;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.MetaMode;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.MetaOID;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.MetaPaddingScheme;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaEntry;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaLength;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaOID;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaPaddingScheme;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.MetaConstructor;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.MetaParameter;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.MetaSpec;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaConstructor;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaParameter;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.exception.InvalidAlgorithmsXMLElementException;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jdom.Element;

class FlexiProviderRootElement {

    private static String version;

    private static List<IMetaAlgorithm> ciphers = new ArrayList<IMetaAlgorithm>();
    private static List<IMetaAlgorithm> macs = new ArrayList<IMetaAlgorithm>();
    private static List<IMetaAlgorithm> blockCiphers = new ArrayList<IMetaAlgorithm>();
    private static List<IMetaAlgorithm> asymmetricBlockCiphers = new ArrayList<IMetaAlgorithm>();
    private static List<IMetaAlgorithm> asymmetricHybridCiphers = new ArrayList<IMetaAlgorithm>();
    private static List<IMetaSpec> parameterSpecs = new ArrayList<IMetaSpec>();
    private static List<IMetaPaddingScheme> paddingSchemes = new ArrayList<IMetaPaddingScheme>();
    private static List<IMetaMode> modes = new ArrayList<IMetaMode>();
    private static List<IMetaKeyGenerator> secretKeyGenerators = new ArrayList<IMetaKeyGenerator>();
    private static List<IMetaKeyGenerator> keyPairGenerators = new ArrayList<IMetaKeyGenerator>();
    private static List<IMetaAlgorithm> messageDigests = new ArrayList<IMetaAlgorithm>();
    private static List<IMetaAlgorithm> secureRandoms = new ArrayList<IMetaAlgorithm>();
    private static List<IMetaAlgorithm> signatures = new ArrayList<IMetaAlgorithm>();

    @SuppressWarnings("incomplete-switch")
    private static void add(RegistryType type, IMetaEntry entry) {
        switch (type) {
            case SECRET_KEY_GENERATOR:
                secretKeyGenerators.add((IMetaKeyGenerator) entry);
                break;
            case KEY_PAIR_GENERATOR: 
                keyPairGenerators.add((IMetaKeyGenerator) entry);
                break;
            case SECURE_RANDOM: 
                secureRandoms.add((IMetaAlgorithm) entry);
                break;
            case MESSAGE_DIGEST: 
                messageDigests.add((IMetaAlgorithm) entry);
                break;
            case CIPHER: 
                ciphers.add((IMetaAlgorithm) entry);
                break;
            case BLOCK_CIPHER: 
                blockCiphers.add((IMetaAlgorithm) entry);
                break;
            case MAC: 
                macs.add((IMetaAlgorithm) entry);
                break;
            case SIGNATURE: 
                signatures.add((IMetaAlgorithm) entry);
                break;
            case ASYMMETRIC_BLOCK_CIPHER:
                asymmetricBlockCiphers.add((IMetaAlgorithm) entry);
                break;
            case ASYMMETRIC_HYBRID_CIPHER:
                asymmetricHybridCiphers.add((IMetaAlgorithm) entry);
                break;
        }

    }

    @SuppressWarnings("unchecked")
    private static void initAlgorithms(RegistryType type, Element algorithmParent) {
        List<Element> messageDigestChildren = new ArrayList<Element>(algorithmParent.getChildren());
        Iterator<Element> it = messageDigestChildren.iterator();
        while (it.hasNext()) {
            Element current = it.next();
            // replace element with blockCipherElement
            String className = current.getAttributeValue(AlgorithmsXMLConstants._class);
            IMetaOID oid = null;
            if (current.getAttributeValue(AlgorithmsXMLConstants._oid) != null) {
                oid = new MetaOID(current.getAttributeValue(AlgorithmsXMLConstants._oid));
            }
            List<String> names = new ArrayList<String>();
            if (current.getChild(AlgorithmsXMLConstants._Names) != null) {
                if (current.getChild(AlgorithmsXMLConstants._Names).getText().contains(",")) { //$NON-NLS-1$
                    StringTokenizer tokenizer =
                            new StringTokenizer(current.getChild(AlgorithmsXMLConstants._Names).getText(), ","); //$NON-NLS-1$
                    while (tokenizer.hasMoreTokens()) {
                        names.add(tokenizer.nextToken());
                    }
                } else {
                    names.add(current.getChild(AlgorithmsXMLConstants._Names).getText());
                }
            }
            IMetaAlgorithm meta = new MetaAlgorithm(type, oid, names, className);
            // // parameter specs
            if (current.getChild(AlgorithmsXMLConstants._ParameterSpec) != null) {
                meta.setParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(
                        AlgorithmsXMLConstants._class));
                if (current.getChild(AlgorithmsXMLConstants._ParameterSpec).getAttribute(
                        AlgorithmsXMLConstants._disabled) == null) {
                    meta.setParameterSpecDisabled(true);
                }
            }
            // parameter generators
            if (current.getChild(AlgorithmsXMLConstants._ParameterGenerator) != null) {
                meta.setParameterGeneratorClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getAttributeValue(
                        AlgorithmsXMLConstants._class));
                meta.setParamGenParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getChild(
                        AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(AlgorithmsXMLConstants._class));
            }

            if (current.getChild(AlgorithmsXMLConstants._StandardParameters) != null) {
                StringTokenizer standardParams =
                        new StringTokenizer(current.getChildText(AlgorithmsXMLConstants._StandardParameters), ","); //$NON-NLS-1$
                while (standardParams.hasMoreTokens()) {
                    meta.addStandardParams(standardParams.nextToken());
                }
            }

            if (current.getAttribute(AlgorithmsXMLConstants._disabled) == null) {
                add(type, meta);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void initKeyGenerators(RegistryType type, Element keyGeneratorParent) {
        List<Element> keyGeneratorChildren = new ArrayList<Element>(keyGeneratorParent.getChildren());
        Iterator<Element> it = keyGeneratorChildren.iterator();
        while (it.hasNext()) {
            Element current = it.next();
            String className = current.getAttributeValue(AlgorithmsXMLConstants._class);
            IMetaKeyGenerator meta = new MetaKeyGenerator(className);
            // oid
            IMetaOID oid = null;
            if (current.getAttributeValue(AlgorithmsXMLConstants._oid) != null) {
                oid = new MetaOID(current.getAttributeValue(AlgorithmsXMLConstants._oid));
                meta.setOID(oid);
            }
            // names
            List<String> names = new ArrayList<String>();
            if (current.getChild(AlgorithmsXMLConstants._Names) != null) {
                if (current.getChild(AlgorithmsXMLConstants._Names).getText().contains(",")) { //$NON-NLS-1$
                    StringTokenizer tokenizer =
                            new StringTokenizer(current.getChild(AlgorithmsXMLConstants._Names).getText(), ","); //$NON-NLS-1$
                    while (tokenizer.hasMoreTokens()) {
                        names.add(tokenizer.nextToken());
                    }
                } else {
                    names.add(current.getChild(AlgorithmsXMLConstants._Names).getText());
                }
            }
            meta.setNames(names);
            // parameter spec
            if (current.getChild(AlgorithmsXMLConstants._ParameterSpec) != null) {
                meta.setParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(
                        AlgorithmsXMLConstants._class));
            }
            // parameter generators
            if (current.getChild(AlgorithmsXMLConstants._ParameterGenerator) != null) {
                meta.setParameterGeneratorClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getAttributeValue(
                        AlgorithmsXMLConstants._class));
                meta.setParamGenParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getChild(
                        AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(AlgorithmsXMLConstants._class));
            }

            // lengths
            if (current.getChild(AlgorithmsXMLConstants._KeyStrengths) != null) {
                int defaultLength =
                        Integer.valueOf(current.getChild(AlgorithmsXMLConstants._KeyStrengths).getAttributeValue(
                                AlgorithmsXMLConstants._default));
                IMetaLength metaLength = new MetaLength(defaultLength);
                if (current.getChild(AlgorithmsXMLConstants._KeyStrengths).getText() != null) {
                    String text = current.getChild(AlgorithmsXMLConstants._KeyStrengths).getText();
                    if (text.contains(",")) { //$NON-NLS-1$
                        List<Integer> lengths = new ArrayList<Integer>(2);
                        StringTokenizer tokenizer = new StringTokenizer(text, ","); //$NON-NLS-1$
                        while (tokenizer.hasMoreTokens()) {
                            lengths.add(Integer.valueOf(tokenizer.nextToken()));
                        }
                        metaLength.setLengths(lengths);
                    } else if (text.contains("...")) { //$NON-NLS-1$
                        int lowerBound = Integer.valueOf(text.substring(0, text.indexOf("..."))); //$NON-NLS-1$
                        int upperBound = Integer.valueOf(text.substring(text.indexOf("...") + 3, text.length())); //$NON-NLS-1$
                        metaLength.setBounds(lowerBound, upperBound);
                    }
                }
                meta.setLengths(metaLength);
            }
            add(type, meta);
        }
    }

    @SuppressWarnings("unchecked")
    private static void initKeyPairAlgorithms(RegistryType type, Element keyPairAlgorithmParent) {
        List<Element> signatureChildren = new ArrayList<Element>(keyPairAlgorithmParent.getChildren());
        Iterator<Element> it = signatureChildren.iterator();
        while (it.hasNext()) {
            Element current = it.next();
            String className = current.getAttributeValue(AlgorithmsXMLConstants._class);
            IMetaOID oid = null;
            if (current.getAttributeValue(AlgorithmsXMLConstants._oid) != null) {
                oid = new MetaOID(current.getAttributeValue(AlgorithmsXMLConstants._oid));
            }
            List<String> names = new ArrayList<String>();
            if (current.getChild(AlgorithmsXMLConstants._Names) != null) {
                if (current.getChild(AlgorithmsXMLConstants._Names).getText().contains(",")) { //$NON-NLS-1$
                    StringTokenizer tokenizer =
                            new StringTokenizer(current.getChild(AlgorithmsXMLConstants._Names).getText(), ","); //$NON-NLS-1$
                    while (tokenizer.hasMoreTokens()) {
                        names.add(tokenizer.nextToken());
                    }
                } else {
                    names.add(current.getChild(AlgorithmsXMLConstants._Names).getText());
                }
            }
            IMetaAlgorithm meta = new MetaAlgorithm(type, oid, names, className);
            // parameter specs
            if (current.getChild(AlgorithmsXMLConstants._ParameterSpec) != null) {
                meta.setParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(
                        AlgorithmsXMLConstants._class));
                if (current.getChild(AlgorithmsXMLConstants._ParameterSpec).getAttribute(
                        AlgorithmsXMLConstants._disabled) == null) {
                    meta.setParameterSpecDisabled(true);
                }
            }
            // parameter generators
            if (current.getChild(AlgorithmsXMLConstants._ParameterGenerator) != null) {
                meta.setParameterGeneratorClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getAttributeValue(
                        AlgorithmsXMLConstants._class));
                meta.setParamGenParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getChild(
                        AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(AlgorithmsXMLConstants._class));
            }

            if (current.getChild(AlgorithmsXMLConstants._StandardParameters) != null) {
                StringTokenizer standardParams =
                        new StringTokenizer(current.getChildText(AlgorithmsXMLConstants._StandardParameters), ","); //$NON-NLS-1$
                while (standardParams.hasMoreTokens()) {
                    meta.addStandardParams(standardParams.nextToken());
                }
            }

            if (current.getAttribute(AlgorithmsXMLConstants._disabled) == null) {
                add(type, meta);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void initModes(Element modesParent) {
        List<Element> modesChildren = new ArrayList<Element>(modesParent.getChildren());
        Iterator<Element> it = modesChildren.iterator();
        while (it.hasNext()) {
            Element current = it.next();
            String className = current.getAttributeValue(AlgorithmsXMLConstants._class);
            String name = current.getAttributeValue(AlgorithmsXMLConstants._name);
            String id = current.getAttributeValue(AlgorithmsXMLConstants._id);
            IMetaMode meta = new MetaMode(className, id, name);
            modes.add(meta);
        }
    }

    @SuppressWarnings("unchecked")
    private static void initPaddingSchemes(Element paddingSchemesParent) {
        List<Element> paddingSchemesChildren = new ArrayList<Element>(paddingSchemesParent.getChildren());
        Iterator<Element> it = paddingSchemesChildren.iterator();
        while (it.hasNext()) {
            Element current = it.next();
            String className = current.getAttributeValue(AlgorithmsXMLConstants._class);
            String id = current.getAttributeValue(AlgorithmsXMLConstants._id);
            String name = current.getAttributeValue(AlgorithmsXMLConstants._name);
            IMetaPaddingScheme meta = new MetaPaddingScheme(className, id, name);
            paddingSchemes.add(meta);
        }
    }

    @SuppressWarnings("unchecked")
    private static void initParameterSpecs(Element parameterSpecsParent) {
        List<Element> algorithmParameterSpecChildren = new ArrayList<Element>(parameterSpecsParent.getChildren());
        Iterator<Element> it = algorithmParameterSpecChildren.iterator();
        while (it.hasNext()) {
            Element currentSpec = it.next();
            String className = currentSpec.getAttributeValue(AlgorithmsXMLConstants._class);
            List<IMetaConstructor> constructors = new ArrayList<IMetaConstructor>();
            List<Element> constructorChildren =
                    new ArrayList<Element>(currentSpec.getChildren(AlgorithmsXMLConstants._Constructor));
            Iterator<Element> consIt = constructorChildren.iterator();
            while (consIt.hasNext()) {
                Element currentConstructor = consIt.next();
                List<IMetaParameter> parameters = new ArrayList<IMetaParameter>();
                List<Element> parameterChildren =
                        new ArrayList<Element>(currentConstructor.getChildren(AlgorithmsXMLConstants._Parameter));
                Iterator<Element> parameterIt = parameterChildren.iterator();
                while (parameterIt.hasNext()) {
                    Element currentParameter = parameterIt.next();
                    String name = currentParameter.getAttributeValue(AlgorithmsXMLConstants._name);
                    String type = currentParameter.getAttributeValue(AlgorithmsXMLConstants._type);
                    String desc = currentParameter.getText();
                    IMetaParameter metaParameter = new MetaParameter(type, name, desc);
                    parameters.add(metaParameter);
                }
                IMetaConstructor metaConstructor = new MetaConstructor(className, parameters);
                constructors.add(metaConstructor);
            }
            IMetaSpec metaSpec = new MetaSpec(className, constructors);
            parameterSpecs.add(metaSpec);
        }
    }

    @SuppressWarnings("unchecked")
    private static void initSecretKeyAlgorithms(RegistryType type, Element secretKeyAlgorithmParent) {
        List<Element> cipherChildren = new ArrayList<Element>(secretKeyAlgorithmParent.getChildren());
        Iterator<Element> it = cipherChildren.iterator();
        while (it.hasNext()) {
            Element current = it.next();
            // replace element with blockCipherElement
            String className = current.getAttributeValue(AlgorithmsXMLConstants._class);
            IMetaOID oid = null;
            if (current.getAttributeValue(AlgorithmsXMLConstants._oid) != null) {
                oid = new MetaOID(current.getAttributeValue(AlgorithmsXMLConstants._oid));
            }
            List<String> names = new ArrayList<String>();
            if (current.getChild(AlgorithmsXMLConstants._Names) != null) {
                if (current.getChild(AlgorithmsXMLConstants._Names).getText().contains(",")) { //$NON-NLS-1$
                    StringTokenizer tokenizer =
                            new StringTokenizer(current.getChild(AlgorithmsXMLConstants._Names).getText(), ","); //$NON-NLS-1$
                    while (tokenizer.hasMoreTokens()) {
                        names.add(tokenizer.nextToken());
                    }
                } else {
                    names.add(current.getChild(AlgorithmsXMLConstants._Names).getText());
                }
            }
            IMetaAlgorithm meta = new MetaAlgorithm(type, oid, names, className);
            // parameter specs
            if (current.getChild(AlgorithmsXMLConstants._ParameterSpec) != null) {
                meta.setParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(
                        AlgorithmsXMLConstants._class));
                if (current.getChild(AlgorithmsXMLConstants._ParameterSpec).getAttribute(
                        AlgorithmsXMLConstants._disabled) == null) {
                    meta.setParameterSpecDisabled(true);
                }
            }
            // parameter generators
            if (current.getChild(AlgorithmsXMLConstants._ParameterGenerator) != null) {
                meta.setParameterGeneratorClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getAttributeValue(
                        AlgorithmsXMLConstants._class));
                meta.setParamGenParameterSpecClassName(current.getChild(AlgorithmsXMLConstants._ParameterGenerator).getChild(
                        AlgorithmsXMLConstants._ParameterSpec).getAttributeValue(AlgorithmsXMLConstants._class));
            }

            if (current.getChild(AlgorithmsXMLConstants._BlockLengths) != null) {
                meta.setDefaultBlockLength(Integer.valueOf(current.getChild(AlgorithmsXMLConstants._BlockLengths).getAttributeValue(
                        AlgorithmsXMLConstants._default)));
                if (current.getChild(AlgorithmsXMLConstants._BlockLengths).getText() != null) {
                    List<Integer> blockLengths = new ArrayList<Integer>(0);
                    StringTokenizer tokenizer =
                            new StringTokenizer(current.getChild(AlgorithmsXMLConstants._BlockLengths).getText(), ","); //$NON-NLS-1$
                    while (tokenizer.hasMoreTokens()) {
                        blockLengths.add(Integer.valueOf(tokenizer.nextToken()));
                    }
                    meta.setBlockLengths(blockLengths);
                }
            }

            if (current.getChild(AlgorithmsXMLConstants._StandardParameters) != null) {
                StringTokenizer standardParams =
                        new StringTokenizer(current.getChildText(AlgorithmsXMLConstants._StandardParameters), ","); //$NON-NLS-1$
                while (standardParams.hasMoreTokens()) {
                    meta.addStandardParams(standardParams.nextToken());
                }
            }

            if (type.equals(RegistryType.MAC)) {
                if (current.getChild(AlgorithmsXMLConstants._BlockCipherReference) != null) {
                    meta.setBlockCipherName(current.getChild(AlgorithmsXMLConstants._BlockCipherReference).getAttributeValue(
                            AlgorithmsXMLConstants._name));
                    if (current.getChild(AlgorithmsXMLConstants._BlockCipherReference).getAttributeValue(
                            AlgorithmsXMLConstants._oid) != null) {
                        meta.setBlockCipherOID(new MetaOID(current.getChild(
                                AlgorithmsXMLConstants._BlockCipherReference).getAttributeValue(
                                AlgorithmsXMLConstants._oid)));
                    }
                    meta.setBlockCipherMode(current.getChild(AlgorithmsXMLConstants._BlockCipherReference).getAttributeValue(
                            AlgorithmsXMLConstants._mode));
                }
            }

            if (current.getAttribute(AlgorithmsXMLConstants._disabled) == null) {
                add(type, meta);
            }
        }
    }

    protected FlexiProviderRootElement(Element element) throws InvalidAlgorithmsXMLElementException {
        if (element.getName().equals(AlgorithmsXMLConstants._FlexiProvider)) {
            version = element.getAttributeValue(AlgorithmsXMLConstants._version);

            // KeyGenerators
            initKeyGenerators(RegistryType.SECRET_KEY_GENERATOR,
                    element.getChild(AlgorithmsXMLConstants._SecretKeyGenerators));
            initKeyGenerators(RegistryType.KEY_PAIR_GENERATOR,
                    element.getChild(AlgorithmsXMLConstants._KeyPairGenerators));

            // Algorithms
            initAlgorithms(RegistryType.SECURE_RANDOM, element.getChild(AlgorithmsXMLConstants._SecureRandoms));
            initAlgorithms(RegistryType.MESSAGE_DIGEST, element.getChild(AlgorithmsXMLConstants._MessageDigests));

            // SecretKeyAlgorithms
            initSecretKeyAlgorithms(RegistryType.BLOCK_CIPHER, element.getChild(AlgorithmsXMLConstants._BlockCiphers));
            initSecretKeyAlgorithms(RegistryType.CIPHER, element.getChild(AlgorithmsXMLConstants._Ciphers));
            initSecretKeyAlgorithms(RegistryType.MAC, element.getChild(AlgorithmsXMLConstants._Macs));

            // KeyPairAlgorithms
            initKeyPairAlgorithms(RegistryType.SIGNATURE, element.getChild(AlgorithmsXMLConstants._Signatures));
            initKeyPairAlgorithms(RegistryType.ASYMMETRIC_BLOCK_CIPHER,
                    element.getChild(AlgorithmsXMLConstants._AsymmetricBlockCiphers));
            initKeyPairAlgorithms(RegistryType.ASYMMETRIC_HYBRID_CIPHER,
                    element.getChild(AlgorithmsXMLConstants._AsymmetricHybridCiphers));

            // misc
            initParameterSpecs(element.getChild(AlgorithmsXMLConstants._ParameterSpecs));
            initModes(element.getChild(AlgorithmsXMLConstants._Modes));
            initPaddingSchemes(element.getChild(AlgorithmsXMLConstants._PaddingSchemes));
        } else {
            throw new InvalidAlgorithmsXMLElementException("Invalid FlexiProviderRootElement!"); //$NON-NLS-1$
        }
    }

    public List<IMetaAlgorithm> getAsymmetricBlockCiphers() {
        return asymmetricBlockCiphers;
    }

    public List<IMetaAlgorithm> getAsymmetricHybridCiphers() {
        return asymmetricHybridCiphers;
    }

    public IMetaAlgorithm getBlockCipher(IMetaOID oid) {
        Iterator<IMetaAlgorithm> it = blockCiphers.iterator();
        while (it.hasNext()) {
            IMetaAlgorithm current = it.next();
            if (current.getOID() != null && current.getOID().equals(oid)) {
                return current;
            }
        }
        return null;
    }

    public IMetaAlgorithm getBlockCipher(String name) {
        Iterator<IMetaAlgorithm> it = blockCiphers.iterator();
        while (it.hasNext()) {
            IMetaAlgorithm current = it.next();
            if (current.isNamed(name)) {
                return current;
            }
        }
        return null;
    }

    public List<IMetaAlgorithm> getBlockCiphers() {
        return blockCiphers;
    }

    public List<IMetaAlgorithm> getCiphers() {
        return ciphers;
    }

    public IMetaKeyGenerator getKeyPairGenerator(String name) {
        Iterator<IMetaKeyGenerator> it = keyPairGenerators.iterator();
        while (it.hasNext()) {
            IMetaKeyGenerator current = it.next();
            if (current.isNamed(name)) {
                return current;
            }
        }
        return null;
    }

    public List<IMetaKeyGenerator> getKeyPairGenerators() {
        return keyPairGenerators;
    }

    public List<IMetaAlgorithm> getMacs() {
        return macs;
    }

    public List<IMetaAlgorithm> getMessageDigests() {
        return messageDigests;
    }

    public IMetaMode getMode(String name) {
        Iterator<IMetaMode> it = modes.iterator();
        while (it.hasNext()) {
            IMetaMode current = it.next();
            if (current.getDescription().equals(name)) {
                return current;
            }
        }
        return null;
    }

    public IMetaMode getModeForID(String id) {
        Iterator<IMetaMode> it = modes.iterator();
        while (it.hasNext()) {
            IMetaMode current = it.next();
            if (current.getID().equals(id)) {
                return current;
            }
        }
        return null;
    }

    public List<IMetaMode> getModes() {
        return modes;
    }

    public IMetaPaddingScheme getPaddingScheme(String name) {
        Iterator<IMetaPaddingScheme> it = paddingSchemes.iterator();
        while (it.hasNext()) {
            IMetaPaddingScheme current = it.next();
            if (current.getPaddingSchemeName().equals(name)) {
                return current;
            }
        }
        return null;
    }

    public IMetaPaddingScheme getPaddingSchemeForID(String id) {
        Iterator<IMetaPaddingScheme> it = paddingSchemes.iterator();
        while (it.hasNext()) {
            IMetaPaddingScheme current = it.next();
            if (current.getID().equals(id)) {
                return current;
            }
        }
        return null;
    }

    public List<IMetaPaddingScheme> getPaddingSchemes() {
        return paddingSchemes;
    }

    public IMetaSpec getParameterSpec(String className) {
        Iterator<IMetaSpec> it = parameterSpecs.iterator();
        while (it.hasNext()) {
            IMetaSpec current = it.next();
            if (current.getClassName().equals(className)) {
                return current;
            }
        }
        return null;
    }

    public IMetaKeyGenerator getSecretKeyGenerator(String name) {
        Iterator<IMetaKeyGenerator> it = secretKeyGenerators.iterator();
        while (it.hasNext()) {
            IMetaKeyGenerator current = it.next();
            if (current.isNamed(name)) {
                return current;
            }
        }
        return null;
    }

    public List<IMetaKeyGenerator> getSecretKeyGenerators() {
        return secretKeyGenerators;
    }

    public List<IMetaAlgorithm> getSecureRandoms() {
        return secureRandoms;
    }

    public IMetaAlgorithm getSignature(IMetaOID oid) {
        Iterator<IMetaAlgorithm> it = signatures.iterator();
        while (it.hasNext()) {
            IMetaAlgorithm current = it.next();
            if (current.getOID() != null && current.getOID().equals(oid)) {
                return current;
            }
        }
        return null;
    }

    public IMetaAlgorithm getSignature(String name) {
        Iterator<IMetaAlgorithm> it = signatures.iterator();
        while (it.hasNext()) {
            IMetaAlgorithm current = it.next();
            if (current.isNamed(name)) {
                return current;
            }
        }
        return null;
    }

    public List<IMetaAlgorithm> getSignatures() {
        return signatures;
    }

    public String getVersion() {
        return version;
    }

}
