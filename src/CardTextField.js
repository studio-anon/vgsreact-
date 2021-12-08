import React from "react";
import { requireNativeComponent } from "react-native";

// eslint-disable-next-line react/prop-types
const NativeView = ({ someRandomProp }) => (
  <CardTextField style={{ flex: 1 }} someRandomProp={someRandomProp} />
);

const CardTextField = requireNativeComponent("VGSCardTextField", NativeView);

export default CardTextField;
